package cn.ruc.edu.hola.client;

public interface QueryTransfer {
    Query CreateQuery(final String config, final String queryID,
                      final String owner, final String date, final String query, final String resp);

    Query ReadQuery(final String queryID);

    String GetAllQueries();
}
