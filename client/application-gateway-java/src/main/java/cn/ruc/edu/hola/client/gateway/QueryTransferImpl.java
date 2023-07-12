package cn.ruc.edu.hola.client.gateway;

import cn.ruc.edu.hola.client.Query;
import cn.ruc.edu.hola.client.QueryTransfer;

public class QueryTransferImpl extends BasicTransfer implements QueryTransfer {
    @Override
    public Query CreateQuery(String config, String queryID, String owner, String date, String query, String resp) {
        return null;
    }

    @Override
    public Query ReadQuery(String queryID) {
        return null;
    }

    @Override
    public String GetAllQueries() {
        return null;
    }
}
