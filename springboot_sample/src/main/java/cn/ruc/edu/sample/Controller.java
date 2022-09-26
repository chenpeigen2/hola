package cn.ruc.edu.sample;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller {
    private Logger logger = LoggerFactory.getLogger(Controller.class);

    @RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String hello() {
        logger.info("get hello ok");
        return "hello";
    }

    @RequestMapping(value = "/test1", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String test1() throws IOException, InterruptedException, ExecutionException {
        return Query.query();
    }

    @RequestMapping(value = "/performance", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String performance() throws IOException, InterruptedException, ExecutionException {
        return Query.queryOkhttp();
    }
}
