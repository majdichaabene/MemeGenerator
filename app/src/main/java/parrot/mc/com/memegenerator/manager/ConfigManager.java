package parrot.mc.com.memegenerator.manager;

public class ConfigManager {
    private String version = "1.0";
    private String url = "http://version1.api.memegenerator.net/";
    private String apiKey = "";

    public ConfigManager() {
    }

    public String getVersion(){
        return version;
    }

    public String getUrl(){
        return url;
    }

    public String getImageByPopularUrl(int pageIndex,int pageSize){
        return url + "/Generators_Select_ByPopular" + "?pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&days=&apiKey=" + apiKey;
    }

    public String getImageByNewUrl(int pageIndex,int pageSize){
        return url + "/Generators_Select_ByNew" + "?pageIndex=" + pageIndex + "&pageSize=" + pageSize + "&days=&apiKey=" + apiKey;
    }

    public String getImageByTrendingUrl(){
        return url + "/Generators_Select_ByTrending" + "?apiKey=" + apiKey;
    }
}
