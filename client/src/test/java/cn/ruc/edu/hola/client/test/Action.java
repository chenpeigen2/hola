package cn.ruc.edu.hola.client.test;

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
                ]
              },
              "execution_stats": true
            }""";
}
