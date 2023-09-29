package cn.ruc.edu.hola.backbend;

import cn.ruc.edu.hola.backbend.parser.FilterParser;
import org.junit.jupiter.api.Test;

public class FilterParserTest {

    @Test
    public void test01() {
        String query = """
                {
                  "selector": {
                    "$or": [
                      {
                        "data.countryid": {
                          "$gt": -1
                        }
                      },
                      {
                        "message": "success"
                      }
                    ],
                    "data.id": 0
                  },
                  "execution_stats": true
                }""";

        var ans = FilterParser.getFilterDbs(query, "bb1");
        System.out.println(ans);
    }
}
