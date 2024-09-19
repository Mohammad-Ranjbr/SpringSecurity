#Encoding  Data
openssl base64 -in plain.txt -out encode.txt

#Decoding Data
openssl base64 -d -in encode.txt -out decode.txt

#Encrypting Data
openssl enc -aes-256-cbc -pass pass:12345 -pbkdf2 -in plain.txt -out encrypt.txt -base64

#Decrypting Data
openssl enc -aes-256-cbc -base64 -pass pass:12345 -d -pbkdf2 -in encrypt.txt -out dencrypt.txt

#Hashing
echo -n "User@123456" | openssl dgst -sha256
openssl dgst -sha256 jdk-22_windows_bin.dmg