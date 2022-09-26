package cn.ruc.edu.sample;

import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Query {
    private static ArrayList<String> urlLists = new ArrayList<>() {{
        add("http://10.77.70.123:5994/workflowchannel_wfscc/file:completeService.bpmn/valueBytes");
        add("http://10.77.70.122:5994/workflowchannel_wfscc/file:completeService.bpmn/valueBytes");
        add("http://10.77.70.173:5994/workflowchannel_wfscc/file:completeService.bpmn/valueBytes");
        //add("http://127.0.0.1:5984/sunzhouxing/e9e881ccd38749197cedaa5bad000e87/valueBytes");
    }};

    public static String query() throws InterruptedException, ExecutionException {
        List<Future<SimpleHttpResponse>> futures = new LinkedList<>();
        for (String url : urlLists) {
            futures.add(Connect.doGet(url));
        }
        List<SimpleHttpResponse> responses = new LinkedList<>();
        for (Future<SimpleHttpResponse> future : futures) {
            responses.add(future.get());
        }
        for (int i = 0; i < responses.size(); ++i) {
            if (!responses.get(i).getBodyText().equals(responses.get(i + 1).getBodyText())) return "false";
        }
        return responses.get(0).getBodyText();
    }

    /*** begin what I start **/
    private final static Dao dao = new Dao("127.0.0.1", 5985);

    public static String queryOkhttp() throws IOException {
        var query = """
                           {
                        "selector": {
                                "data.countryid": {"${'$'}gt": -1}
                            },
                            "execution_stats": true
                        }
                """.trim();

        try (var res = dao.filterDocs("fake0", query)) {
            return res.body().string();
        }

    }
}
