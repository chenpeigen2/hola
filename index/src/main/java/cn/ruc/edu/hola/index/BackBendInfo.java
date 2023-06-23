package cn.ruc.edu.hola.index;

import java.util.*;


public class BackBendInfo {

    public static class PathMapping {

        private final String path;

        private final Set<String> keywords;

        public PathMapping(String path) {
            this.path = path;
            keywords = new HashSet<>();
        }

        public void put(String keyword) {
            keywords.add(keyword);
        }
    }

    private final String url;

    private final List<PathMapping> pathList;

    public BackBendInfo(String url) {
        this.url = url;
        pathList = new ArrayList<>();
    }

    public void putPath(PathMapping pathMapping) {
        pathList.add(pathMapping);
    }

}