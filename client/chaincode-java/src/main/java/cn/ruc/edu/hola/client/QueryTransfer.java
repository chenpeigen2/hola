package cn.ruc.edu.hola.client;

import java.util.List;

public interface QueryTransfer {
    Query CreateQuery(final String config, final String queryID,
                      final String owner, final String date, final String query, final String resp);

    List<Query> CreateMultiQuery(final String sgxConfig, final String queries);

    Query ReadQuery(final String queryID);

    String GetAllQueries();
}
