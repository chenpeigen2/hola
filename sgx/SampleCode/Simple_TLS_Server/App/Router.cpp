//
// Created by chenpeigen on 23-2-12.
//
#include "Router.h"

#include <wfrest/json.hpp>

#include "HttpClient.h"
#include <iostream>

using namespace wfrest;
using namespace std;
using Json = nlohmann::json;

void router(wfrest::HttpServer &server) {
    /** filter complex for **/
    server.POST("/_complex", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
        }
        // 遍历数组并输出每个元素
        auto data = req->json().dump();
        std::string result;
//        result = complex_filter(data);
        result = "KaEoKWYe5+uVGSbt1eR8nooQhx5rYmwAhujBPRBnmiml4QuRFW0BSrAXDHzf5dLiUVlcYhU0GYgcuH5T/1BtSQCkoaJ4i54UkSs4MbE1JiIPmGEII4zY4iQyuV955l4wtKPxhJmB+VIJFJLY3+dpHDB6cGvFAourgFKEGqOHzx5t5dnTMMa1MDj+kOsg0Je07OgIQWlkiYL/3aXiPFLAJhkVi6TQ1eVNjv+mB2qgLI7I05K/mc5AoZ+nzDy+k1so9sYYceYq/CzG6e5tbysFh36RQ+/o/3RaPkrkUNHsE70uErOvTq34awxiQI25ybYt4KF4+Q5Obhbiw1gn6EJhvKqNpIafIZpO1h90lxdb95/MyzOxE660aF6JkXFo5mBBF1N3SI6OTXl2L6/etGncNnfpI+oJJF7Z6iDmUQhuJt+qOeVxRqTp4hPaTV0pIz4OgHKJufNWn1rK4umOwglbV9W5GFIw59Nw+C1zkWGRuSf+W4YPnSCk8rZCbo3nCwABlv4MSEaVMXEtak+5s1Vivog5rTtt3jBNckxHNEz92sJMQvUzJUUhMo7bu9JyE6rrWAqdXOnGhpBb3dgEp8o3dm6pTFKrTAYLMJl3MIir+c1N32kFLsk7+S8fFloPuXA5bRN31qWCZ+LFsF3qLYr+js2zkOcWGKgFI7WKshsFvZFhnWnXSc2UJ7ab2+L+qjSaa1d3ZA2kV7a6A5oFCyGDfPh96WDHggvE5YeOd2JwVZyvAHBO0ZlD0Vcpc5NkkChTnZZkZkqTttBfH2DQ5WpGQUH+wS8tcAXA3yWtLOFlLSF6ld56DAp8LxtnAc9sXuTGqagDRvPAPC3KCnJ9fUO/r300BT4PbM6ohZpIcysdCgVjcD5P87Z8IWMAHz7aDPqYEXrDJGqLHIoy0bl0szju9CJXcRcN0/1b/kPJQs4kWHwa5+elqmDHErTxya7HmGVr";
        std::cout << "the data from the encalve: " << result << std::endl;
        resp->String(result);
    });

    server.POST("/_auth", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
            return;
        }
        auto data = req->json().dump();
        auto ret = auth_sgx(data);
        fprintf(stderr, "Json : %s", req->json().dump(4).c_str());
    });

    server.GET("/hello", [](const HttpReq *req, HttpResp *resp) {
        resp->String("world !!!!!!111111)\n");
    });
}