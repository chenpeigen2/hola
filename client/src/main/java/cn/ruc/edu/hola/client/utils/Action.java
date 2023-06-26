package cn.ruc.edu.hola.client.utils;

public interface Action {
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
                "data.country": "China"
              },
              "execution_stats": true
            }""";
}
