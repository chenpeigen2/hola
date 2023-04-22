//
// Created by chenpeigen on 23-4-22.
//
//
// Created by oceanstar on 2021/8/6.
//

#include <iostream>
#include <string.h>
#include "http_utils.h"
#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "string.h"
#include "string"


namespace oceanstar {
    using namespace std;

#define    HTTP_PREFIX    "http://"
#define    HTTPS_PREFIX    "https://"

    bool http_utils::get_addr(const char *url, char *domain, size_t size,
                              unsigned short *pport) {
        const char *ptr;
        unsigned short default_port;

        if (!strncasecmp(url, HTTP_PREFIX, sizeof(HTTP_PREFIX) - 1)) {
            ptr = url + sizeof(HTTP_PREFIX) - 1;
            default_port = 80;
        } else if (!strncasecmp(url, HTTPS_PREFIX, sizeof(HTTPS_PREFIX) - 1)) {
            ptr = url + sizeof(HTTPS_PREFIX) - 1;
            default_port = 443;
        } else {
            printf("invalid url: %s", url);
            return false;
        }

        if (*ptr == 0) {
            printf("invalid url: %s", url);
            return false;
        }

        char buf[256];
        strncpy(buf, ptr, sizeof(buf));

        char *slash = strchr(buf, '/');
        if (slash) {
            *slash = 0;
        }

        unsigned short port;

        char *col = strchr(buf, ':');
        if (col == NULL) {
            port = default_port;
        } else {
            *col++ = 0;
            port = (unsigned short) atoi(col);
            if (port == 0 || port == 65535) {
                port = default_port;
            }
        }

        if (pport) {
            *pport = port;
        }
        strncpy(domain, buf, size);
        return true;
    }


    bool http_utils::get_addr(const char *url, char *addr, size_t size) {
        char buf[256];
        unsigned short port;

        if (!get_addr(url, buf, sizeof(buf), &port)) {
            return false;
        }
        snprintf(addr, size, "%s:%d", buf, port);
        return true;
    }
}

namespace oceanstar {

    using namespace std;

    http_url::http_url(void) {
        strncpy(proto_, "http", sizeof(proto_));
        port_ = 80;
    }

    void http_url::reset(void) {
        strncpy(proto_, "http", sizeof(proto_));
        port_ = 80;
        domain_.clear();
        url_path_.clear();
        url_params_.clear();
    }

    const char *http_url::parse_domain(const char *url) {
        if (*url == '/') {
            printf("invalid url: %s", url);
            return NULL;
        }
        const char *ptr = strchr(url, '/');
        if (ptr == NULL) {
            domain_ = url;
            return NULL;
        }

        char buf[256];
        size_t size = ptr - url + 1;
        if (size > sizeof(buf)) {
            size = sizeof(buf);
        }
        strncpy(buf, url, size);


        char *col = strchr(buf, ':');
        if (col != NULL) {
            *col++ = 0;
            port_ = (unsigned short) atoi(col);
            if (port_ == 0 || port_ == 65535) {
                port_ = 80;
            }
        }
        domain_ = buf;
        return ptr;
    }


    bool http_url::parse(const char *url) {
        const char *ptr;

        if (!strncasecmp(url, HTTP_PREFIX, sizeof(HTTP_PREFIX) - 1)) {
            ptr = url + sizeof(HTTP_PREFIX) - 1;
        } else if (!strncasecmp(url, HTTPS_PREFIX, sizeof(HTTPS_PREFIX) - 1)) {
            ptr = url + sizeof(HTTPS_PREFIX) - 1;
            port_ = 443;
            strncpy(proto_, "https", sizeof(proto_));
        } else if (*url == '/') {
            ptr = url;
        } else {
            printf("invalid url: %s", url);
            return false;
        }

        if (*ptr == 0) {
            printf("invalid url: %s", url);
            return false;
        }

        if (ptr == url) {
            // 说明是仅含相对路径的 url
            return parse_url_part(url);
        } else {
            // 说明包含有完整路径的 url，下面先提取域名字段，再提取相对 url
            ptr = parse_domain(ptr);
            if (ptr == NULL) {
                url_path_ = "/";
                return true;
            }
            return parse_url_part(ptr);
        }
    }

#define SKIP_WHILE(cond, ptr) { while(*ptr && (cond)) ptr++; }

    bool http_url::parse_url_part(const char *url) {
        if (*url != '/') {
            printf("invalid url: %s", url);
            return false;
        }

        const char *ptr = url;
        SKIP_WHILE(*ptr == '/', ptr);
        if (*ptr == 0) {
            url_path_ = "/";
            return true;
        }

        const char *qm = strchr(ptr, '?');
        if (qm == NULL) {
            url_path_ = url;
            return true;
        }
        char buf[strlen(url)];
        strncpy(buf, url, qm - url);
        url_path_ = buf;
        ++qm;
        if (*qm != 0) {
            url_params_ = qm;
        }
        return true;
    }

}

