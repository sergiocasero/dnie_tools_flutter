package com.sergiocasero.dnie_tools_flutter

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import es.gob.jmulticard.jse.provider.DnieKeyStore
import es.gob.jmulticard.jse.provider.DnieProvider
import es.gob.jmulticard.jse.provider.MrtdKeyStoreImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.security.Security

class DnieReader(
        private val activity: Activity,
        private val block: (DnieResponse) -> Unit
) : NfcAdapter.ReaderCallback {

    private var can: String = ""

    private val nfc by lazy { NfcAdapter.getDefaultAdapter(activity) }

    fun resume(can: String) {
        send(DnieResponse.Initializing)
        this.can = can
        val options = Bundle()
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
        nfc.enableReaderMode(
                activity,
                this,
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or NfcAdapter.FLAG_READER_NFC_B,
                options
        )
        send(DnieResponse.Ready)
    }

    fun stop() {
        nfc.disableReaderMode(activity)
    }

    private fun send(response: DnieResponse) {
        GlobalScope.launch(Dispatchers.Main) { block(response) }
    }

    override fun onTagDiscovered(p0: Tag?) {

        try {
            send(DnieResponse.InProgress(DnieResponse.InProgress.ProgressType.INFO))
            val provider = DnieProvider()
            provider.providerTag = p0
            provider.providerCan = can

            Security.insertProviderAt(provider, 1)

            val storeSpi = MrtdKeyStoreImpl()
            val keystore = DnieKeyStore(storeSpi, provider, "MRTD")
            keystore.load(null)

            // Conexión con el DNIe
            val info = DnieResponse.DnieInfo(
                    birth = keystore.datagroup1.dateOfBirth,
                    expiry = keystore.datagroup1.dateOfExpiry,
                    docNumber = keystore.datagroup1.docNumber,
                    nationality = keystore.datagroup1.nationality,
                    name = keystore.datagroup1.name,
                    surname = keystore.datagroup1.surname,
                    expeditionCountry = keystore.datagroup1.issuer,
                    nif = keystore.datagroup11.personalNumber,
                    sex = keystore.datagroup1.sex,
                    address = keystore.datagroup11.getAddress(1),
                    city = keystore.datagroup11.getAddress(2),
                    state = keystore.datagroup11.getAddress(3)
            )

            send(info)
            send(DnieResponse.InProgress(DnieResponse.InProgress.ProgressType.PICTURE))
            send(DnieResponse.Image(saveImage(info.nif, DnieResponse.Image.ImageType.PICTURE, keystore.datagroup2.imageBytes), DnieResponse.Image.ImageType.PICTURE))

            send(DnieResponse.InProgress(DnieResponse.InProgress.ProgressType.SIGN))
            send(DnieResponse.Image(saveImage(info.nif, DnieResponse.Image.ImageType.SIGN, keystore.datagroup7.imageBytes), DnieResponse.Image.ImageType.SIGN))
        } catch (e: Exception) {
            val message = e.message
            if (message != null) {
                send(DnieResponse.Error(message))
            }
        }
    }

    private fun saveImage(id: String, type: DnieResponse.Image.ImageType, bytes: ByteArray): String {
        val j2k = J2kStreamDecoder()
        val bis = ByteArrayInputStream(bytes)

        val bitmap = j2k.decode(bis)

        val imageName = "$id-$type.png"
        val fos = activity.openFileOutput(imageName, Context.MODE_PRIVATE)

        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos)

        fos.flush()
        fos.close()

        return "${activity.filesDir.absolutePath}/$imageName"
    }
}

sealed class DnieResponse {
    object Initializing : DnieResponse()
    object Ready : DnieResponse()

    data class InProgress(val type: ProgressType) : DnieResponse() {
        enum class ProgressType {
            INFO, PICTURE, SIGN
        }
    }

    data class DnieInfo(
            val nif: String,
            val birth: String,
            val expiry: String,
            val docNumber: String,
            val expeditionCountry: String,
            val sex: String,
            val nationality: String,
            val name: String,
            val surname: String,
            val address: String,
            val city: String,
            val state: String
    ) : DnieResponse()

    data class Image(val image: String, val imageType: ImageType) : DnieResponse() {
        enum class ImageType {
            PICTURE, SIGN
        }
    }

    data class Error(val message: String) : DnieResponse()
}

