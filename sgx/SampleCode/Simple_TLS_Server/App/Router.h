//
// Created by chenpeigen on 23-2-12.
//

#ifndef SGX_ROUTER_H
#define SGX_ROUTER_H

#include <wfrest/HttpServer.h>

#if defined(__cplusplus)
extern "C" {
#endif

void router(wfrest::HttpServer &svr);

#if defined(__cplusplus)
}
#endif
#endif //SGX_ROUTER_H
