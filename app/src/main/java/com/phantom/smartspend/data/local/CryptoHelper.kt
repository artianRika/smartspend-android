import android.content.Context
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object CryptoHelper {

    private const val KEY_ALIAS = "smartspend_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    private fun getSecretKey(context: Context): SecretKey {
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

        // Check if key exists
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance("AES", ANDROID_KEYSTORE)
            keyGenerator.init(android.security.keystore.KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or android.security.keystore.KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            )
            keyGenerator.generateKey()
        }

        return (keyStore.getKey(KEY_ALIAS, null) as SecretKey)
    }

    fun encrypt(context: Context, plaintext: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(context))
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plaintext.toByteArray())
        val combined = iv + encrypted
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decrypt(context: Context, encryptedText: String): String {
        val bytes = Base64.decode(encryptedText, Base64.NO_WRAP)
        val iv = bytes.copyOfRange(0, 12)  // 12 bytes IV for GCM
        val encrypted = bytes.copyOfRange(12, bytes.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(context), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(encrypted))
    }
}
