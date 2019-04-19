import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public static final double ROOT_LRLON = -122.2119140625;
    public static final double ROOT_ULLON = -122.2998046875;
    public static final double ROOT_ULLAT = 37.892195547244356;
    public static final double ROOT_LRLAT = 37.82280243352756;
    public static final int TILE_SIZE = 256;
    public static final double L0LonDPP = (ROOT_LRLON - ROOT_ULLON)/TILE_SIZE;
    //public static final double L0LonDPP = 0.00034332275390625;


    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        double ulLon = params.get("ullon");
        double lrLon = params.get("lrlon");
        double ulLat = params.get("ullat");
        double lrLat = params.get("lrlat");
        double width = params.get("w");
        double height = params.get("h");

        Map<String, Object> results = new HashMap<>();
        if (ulLon > lrLon || ulLat < lrLat || lrLon < ROOT_ULLON
                || ulLon > ROOT_LRLON || ulLat < ROOT_LRLAT || lrLat > ROOT_ULLAT) {
            results.put("render_grid", new String[1][1]);
            results.put("raster_ul_lon", 1.0);
            results.put("raster_ul_lat", 1.0);
            results.put("raster_lr_lon", 1.0);
            results.put("raster_lr_lat", 1.0);
            results.put("depth", 1.0);
            results.put("query_success", false);
            System.out.println("first case");
        }

/* two requirements:
 b.Have the greatest LonDPP that is less than or equal to
 the LonDPP of the query box - this is to find the depth;
 step1: calculate the LonDPP of the query box
    queryLonDPP = (lrLon- ulLon)/width;
 step2: calculate the LonDPP for each level
 to find the picture level order
the picture LonDDP decrease by 2 as the level increases
   L0LonDPP = 0.00034332275390625;
 step3: compare the LonDDp
 picLonDDP <= queryLonDPP;
 picLevelUpDDP > queryLonDPP;
 we can use log function to find the result;
 picLonDPP = L0LonDPP* (1/2)^(order);
 depth = (log(L0LonDPP) - log(queryLonDPP))/log(2)
 */

        double queryLonDPP = (lrLon- ulLon)/width;
        double perdepth = (Math.log(L0LonDPP)-Math.log(queryLonDPP))/ Math.log(2.0);
        int depth = (int) perdepth+1;
        if(depth>7){
            depth = 7;
        }

        System.out.println("depth is " +depth);
        /*
        a. include any region of the query box - this is to find the boundary.
 such that the
 display [0][0] raster_ul_lon < ulLon;
 display [0][0] raster_ul_lat > ulLat;
 find the dimension of the display
 Given ulLon = m X + ROOT_ULLON; m = (ROOT_ULLON-ROOT_LRLON)/(depth^2);
 solve for x will know the index of the first image;
percisX  = (ulLon-ROOT_LRLON)/(ROOT_ULLON-ROOT_LRLON)/(depth^2);
x0 = (int) percisX  /*this covers more to the left
in general the picture size is (3 rows of 4 images each)
so xk = x0 + 3; yk  = y0+2;
        * */
        int dmax = (int) Math.pow(2,depth) - 1;

        double mx  = (ROOT_LRLON-ROOT_ULLON)/(Math.pow(2.0,depth));

        double perXL = (ulLon-ROOT_ULLON)/mx;
        int XL = (int) perXL;
        double raster_ul_lon = ROOT_LRLON+mx*XL;

        double perXR = (lrLon-ROOT_ULLON)/mx;
        int XR = (int) perXR;
        //check if xr exist
        if(XR > dmax){
            XR--;
        }
        double raster_lr_lon = ROOT_LRLON+mx*(XR+1);


        double my  = (ROOT_LRLAT-ROOT_ULLAT)/(Math.pow(2.0,depth));
        double perYU = (ulLat-ROOT_ULLAT)/my;
        int YU = (int) perYU;
        double raster_ul_lat = ROOT_LRLON+my*YU;

        double perYD = (lrLat-ROOT_ULLAT)/my;
        int YD = (int) perYD;
        //check if YD exist
        if(YD > dmax){
            YD--;
        }
        double raster_lr_lat = ROOT_LRLON+my*(YD+1);

        /*
        System.out.println("x0 is " +XL);
        System.out.println("xk is " +XR);
        System.out.println("y0 is " +YU);
        System.out.println("uk is " +YD);
        */

        /*
        Figure out how many tiles you’ll need.

        * */
        int dx = XR - XL+1;
        int dy = YD - YU+1;
        /*if the image exist dx+1
        int dmax = (int) Math.pow(2,depth) - 1;
        if(dx < dmax){
            dx++;
        }
        if(dy< dmax){
            dy++;
        }
        */


        String[][] display = new String[dy][dx];
        // put into the sentence

        for(int i = 0; i < dx;i++){
            for(int j = 0; j < dy; j++){
                String a = "d"+depth+"_x"+(XL+i)+"_y"+(YU+j)+".png";
                //System.out.println(a);
                display[j][i] = a;

            }
        }

        results.put("render_grid", display);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", true);
        return results;

    }
}

