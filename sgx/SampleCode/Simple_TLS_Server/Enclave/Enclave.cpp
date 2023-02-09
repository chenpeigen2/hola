#include "Enclave_t.h"
#include "Ocall_wrappers.h"

#include <openssl/ssl.h>
#include <openssl/x509.h>

#define KEY_LENGTH  2048             // 密钥长度

static void init_openssl()
{
	OpenSSL_add_ssl_algorithms();
	OpenSSL_add_all_ciphers();
	SSL_load_error_strings();
}

static void cleanup_openssl()
{
    EVP_cleanup();
}

static SSL_CTX *create_context()
{
    const SSL_METHOD *method;
    SSL_CTX *ctx;

    method = TLSv1_2_method();

    ctx = SSL_CTX_new(method);
    if (!ctx) {
        printe("Unable to create SSL context");
        exit(EXIT_FAILURE);
    }
    return ctx;
}

static int password_cb(char *buf, int size, int rwflag, void *password)
{
    strncpy(buf, (char *)(password), size);
    buf[size - 1] = '\0';
    return strlen(buf);
}

static EVP_PKEY *generatePrivateKey()
{
    EVP_PKEY *pkey = NULL;
    EVP_PKEY_CTX *pctx = EVP_PKEY_CTX_new_id(EVP_PKEY_RSA, NULL);
    EVP_PKEY_keygen_init(pctx);
    EVP_PKEY_CTX_set_rsa_keygen_bits(pctx, 2048);
    EVP_PKEY_keygen(pctx, &pkey);
    return pkey;
}

static X509 *generateCertificate(EVP_PKEY *pkey)
{
    X509 *x509 = X509_new();
    X509_set_version(x509, 2);
    ASN1_INTEGER_set(X509_get_serialNumber(x509), 0);
    X509_gmtime_adj(X509_get_notBefore(x509), 0);
    X509_gmtime_adj(X509_get_notAfter(x509), (long)60*60*24*365);
    X509_set_pubkey(x509, pkey);

    X509_NAME *name = X509_get_subject_name(x509);
    X509_NAME_add_entry_by_txt(name, "C", MBSTRING_ASC, (const unsigned char*)"US", -1, -1, 0);
    X509_NAME_add_entry_by_txt(name, "CN", MBSTRING_ASC, (const unsigned char*)"YourCN", -1, -1, 0);
    X509_set_issuer_name(x509, name);
    X509_sign(x509, pkey, EVP_md5());
    return x509;
}

/*
制造密钥对：私钥和公钥
**/
void GenerateRSAKey() {
    size_t pri_len = 0; // 私钥长度
    size_t pub_len = 0; // 公钥长度
    char *pri_key = nullptr; // 私钥
    char *pub_key = nullptr; // 公钥

    // 生成密钥对
    RSA *keypair = RSA_generate_key(KEY_LENGTH, RSA_3, nullptr, nullptr);

    BIO *pri = BIO_new(BIO_s_mem());
    BIO *pub = BIO_new(BIO_s_mem());

    // 生成私钥
    PEM_write_bio_RSAPrivateKey(pri, keypair, nullptr, nullptr, 0, nullptr, nullptr);
    // 注意------生成第1种格式的公钥
    //PEM_write_bio_RSAPublicKey(pub, keypair);
    // 注意------生成第2种格式的公钥（此处代码中使用这种）
    PEM_write_bio_RSAPublicKey(pub, keypair);

    // 获取长度
    pri_len = BIO_pending(pri);
    pub_len = BIO_pending(pub);

    // 密钥对读取到字符串
    pri_key = (char *) malloc(pri_len + 1);
    pub_key = (char *) malloc(pub_len + 1);

    BIO_read(pri, pri_key, pri_len);
    BIO_read(pub, pub_key, pub_len);

    pri_key[pri_len] = '\0';
    pub_key[pub_len] = '\0';

//    out_pub_key = pub_key;
//    out_pri_key = pri_key;

//    // 将公钥写入文件
//    std::ofstream pub_file(PUB_KEY_FILE, std::ios::out);
//    if (!pub_file.is_open()) {
//        perror("pub key file open fail:");
//        return;
//    }
//    pub_file << pub_key;
//    pub_file.close();
//
//    // 将私钥写入文件
//    std::ofstream pri_file(PRI_KEY_FILE, std::ios::out);
//    if (!pri_file.is_open()) {
//        perror("pri key file open fail:");
//        return;
//    }
//    pri_file << pri_key;
//    pri_file.close();
    printl("fffffffffffffffffffffffffff...");
    printl("fffffffffffffffffffffffffff...");
    printl("fffffffffffffffffffffffffff...");

    // 释放内存
    RSA_free(keypair);
    BIO_free_all(pub);
    BIO_free_all(pri);

    free(pri_key);
    free(pub_key);
}

static void configure_context(SSL_CTX *ctx)
{
	EVP_PKEY *pkey = generatePrivateKey();
	X509 *x509 = generateCertificate(pkey);

	SSL_CTX_use_certificate(ctx, x509);
	SSL_CTX_set_default_passwd_cb(ctx, password_cb);
	SSL_CTX_use_PrivateKey(ctx, pkey);

	RSA *rsa=RSA_generate_key(512, RSA_F4, NULL, NULL);
	SSL_CTX_set_tmp_rsa(ctx, rsa);
	RSA_free(rsa);

	SSL_CTX_set_verify(ctx, SSL_VERIFY_NONE, 0);
}

static int create_socket_server(int port)
{


    int s, optval = 1;
    struct sockaddr_in addr;

    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    addr.sin_addr.s_addr = htonl(INADDR_ANY);

    s = socket(AF_INET, SOCK_STREAM, 0);
    if (s < 0) {
        printe("sgx_socket");
        exit(EXIT_FAILURE);
    }
    if (setsockopt(s, SOL_SOCKET, SO_REUSEADDR, (const void *)&optval, sizeof(int)) < 0) {
        printe("sgx_setsockopt");
        exit(EXIT_FAILURE);
    }
    if (bind(s, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
        printe("sgx_bind");
        exit(EXIT_FAILURE);
    }
    if (listen(s, 128) < 0) {
        printe("sgx_listen");
        exit(EXIT_FAILURE);
    }
    return s;
}

void ecall_start_tls_server(void)
{
    int sock;
    SSL_CTX *ctx;

    printl("OPENSSL Version = %s", SSLeay_version(SSLEAY_VERSION));
    init_openssl();
    ctx = create_context();
    configure_context(ctx);

    sock = create_socket_server(4433);
    if(sock < 0) {
        printe("create_socket_client");
        exit(EXIT_FAILURE);
    }

    /* Handle SSL/TLS connections */
    GenerateRSAKey();
    while(1) {
        struct sockaddr_in addr;
        int len = sizeof(addr);
        SSL *cli;
        unsigned char read_buf[1024];
        int r = 0;
        printl("Wait for new connection...");
        int client = accept(sock, (struct sockaddr*)&addr, &len);
        if (client < 0) {
            printe("Unable to accept");
            exit(EXIT_FAILURE);
        }

	cli = SSL_new(ctx);
        SSL_set_fd(cli, client);
	if (SSL_accept(cli) <= 0) {
            printe("SSL_accept");
            exit(EXIT_FAILURE);
        }
		
        printl("ciphersuit: %s", SSL_get_current_cipher(cli)->name);
        /* Receive buffer from TLS server */
        r = SSL_read(cli, read_buf, sizeof(read_buf));
        printl("read_buf: length = %d : %s", r, read_buf);
        memset(read_buf, 0, sizeof(read_buf));        
        
        printl("Close SSL/TLS client");
        SSL_free(cli);
        sgx_close(client);
    }

    sgx_close(sock);
    SSL_CTX_free(ctx);
    cleanup_openssl();
}
