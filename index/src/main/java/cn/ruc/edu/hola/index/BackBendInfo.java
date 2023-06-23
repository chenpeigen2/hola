package cn.ruc.edu.hola.index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BackBendInfo {

    public String url() {
        return url;
    }

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


        @Override
        public String toString() {
            return "PathMapping{" +
                    "path='" + path + '\'' +
                    ", keywords=" + keywords +
                    '}';
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

    public String getPath(List<String> keyWords) {
        String result = null;
        for (PathMapping mapping : pathList) {
            boolean contained = true;
            for (String keyWord : keyWords) {
                if (!mapping.keywords.contains(keyWord)) {
                    contained = false;
                    break;
                }
            }
            if (contained) {
                result = mapping.path;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "BackBendInfo{" +
                "url='" + url + '\'' +
                ", pathList=" + pathList +
                '}';
    }
}