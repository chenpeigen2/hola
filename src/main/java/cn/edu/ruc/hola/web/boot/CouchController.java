package cn.edu.ruc.hola.web.boot;

import cn.edu.ruc.hola.benchmark.Bench;
import cn.edu.ruc.hola.entity.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class CouchController {

    /**
     * {
     * "th" : 0.95,
     * "M" : 100,
     * "H" : 67,
     * "query" : {"selector":{"data.countryid":{"$gt":-1}},"execution_stats":true}
     * }
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */

    @RequestMapping(value = "/postQuery", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Response performance() throws IOException, InterruptedException, ExecutionException {
        float threshHold = 0.95f;
        int M = 100;
        int H = 67;
        var query = "{\"selector\":{\"data.countryid\":{\"$gt\":-1}},\"execution_stats\":true}";
        return Bench.INSTANCE.execute(threshHold, M, H, query);
    }
}
