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
    String queryStu = """
            {
              "selector": {
                "$or": [
                  {
                    "class_id": "17二班"
                  },
                  {
                    "class_id": "17一班"
                  }
                ],
                "gender": 0
              },
              "execution_stats": true
            }""";

    String querySocial = """
            {
              "selector": {
                "$or": [
                  {
                    "social_id": "乒乓球"
                  },
                  {
                    "social_id": "羽毛球"
                  }
                ],
                "gender": 0
              },
              "execution_stats": true
            }""";
}
