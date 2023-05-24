package cn.edu.ruc.hola.fake;

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
                "data.id": 0
              },
              "execution_stats": true
            }""";
}
