package com.myinstagram.afinski.myinstgrmviewer.util;

/**
 * Created by afinski on 05.01.2015.
 */
public class PictureInstagram {
    private final String id;
    private String url_thumbnail;
    private String url_low_resolution;
    private String url_standard_resolution;

    private ImageSize size_thumbnail;
    private ImageSize size_low_resolution;
    private ImageSize size_standard_resolution;


    /*
            "images":{
            "low_resolution":{"url":"http:\/\/scontent-b.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10860121_402347009943400_508494121_a.jpg","width":306,"height":306},
            "thumbnail":{"url":"http:\/\/scontent-b.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10860121_402347009943400_508494121_s.jpg","width":150,"height":150},
            "standard_resolution":{"url":"http:\/\/scontent-b.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10860121_402347009943400_508494121_n.jpg","width":640,"height":640}
            },
         */
    public PictureInstagram(String id){
        this.id = id;
    }


    public String getUrl_thumbnail() {
        return url_thumbnail;
    }

    public void setUrl_thumbnail(String url_thumbnail) {
        this.url_thumbnail = url_thumbnail;
    }

    public String getUrl_low_resolution() {
        return url_low_resolution;
    }

    public void setUrl_low_resolution(String url_low_resolution) {
        this.url_low_resolution = url_low_resolution;
    }

    public String getUrl_standard_resolution() {
        return url_standard_resolution;
    }

    public void setUrl_standard_resolution(String url_standard_resolution) {
        this.url_standard_resolution = url_standard_resolution;
    }

    public ImageSize getSize_thumbnail() {
        if(size_thumbnail==null){
            size_thumbnail = new ImageSize();
        }
        return size_thumbnail;
    }

    public void setSize_thumbnail(ImageSize size_thumbnail) {
        this.size_thumbnail = size_thumbnail;
    }

    public ImageSize getSize_low_resolution() {
        if(size_low_resolution==null){
            size_low_resolution = new ImageSize();
        }
        return size_low_resolution;
    }

    public void setSize_low_resolution(ImageSize size_low_resolution) {
        this.size_low_resolution = size_low_resolution;
    }

    public ImageSize getSize_standard_resolution() {
        if(size_standard_resolution==null){
            size_standard_resolution = new ImageSize();
        }
        return size_standard_resolution;
    }

    public void setSize_standard_resolution(ImageSize size_standard_resolution) {
        this.size_standard_resolution = size_standard_resolution;
    }


    public class ImageSize{
        private int  imageWidth;
        private int  imageHeight;

        public int getImageWidth() {
            return imageWidth;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public int getImageHeight() {
            return imageHeight;
        }

        public void setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
        }
    }
}
/*
{"data":
    [
        {
        "tags":["tuija","selfie","stagshead"],
        "location":null,
        "link":"http:\/\/instagram.com\/p\/xMoE4xIsIX\/",
        "caption":
        {
        "id":"886259484863939513","created_time":"1419870385",
        "text":"Tuijia tyhmissä paikoissa -sarja lopetetaan tarpeettomana. Mä en pysty keksimään tyhmempää paikkaa kuin tuija runkkarin kainalossa. #stagshead #tuija #selfie",
        "from":
        {
        "id":"328287223",
        "profile_picture":"https:\/\/igcdn-photos-c-a.akamaihd.net\/hphotos-ak-xpa1\/t51.2885-19\/10661074_1491498807790266_1996311954_a.jpg",
        "username":"siltamaeki","full_name":"siltamaeki"
        }
        },
        "type":"image",
        "id":"886259484377399831_328287223",
        "likes":{"data":[],"count":0},
        "images":
        {
        "low_resolution":
        {
        "url":"http:\/\/scontent-a.cdninstagram.com\/hphotos-xfa1\/t51.2885-15\/10848386_796666763714787_911175744_a.jpg",
        "height":306,"width":306
        },
        "standard_resolution":
        {
        "url":"http:\/\/scontent-a.cdninstagram.com\/hphotos-xfa1\/t51.2885-15\/10848386_796666763714787_911175744_n.jpg",
        "height":640,"width":640
        },
        "thumbnail":
        {
        "url":"http:\/\/scontent-a.cdninstagram.com\/hphotos-xfa1\/t51.2885-15\/10848386_796666763714787_911175744_s.jpg",
        "height":150,"width":150
        }
        },
        "users_in_photo":[],
        "created_time":"1419870385",
        "user":
        {
        "id":"328287223",
        "profile_picture":"https:\/\/igcdn-photos-c-a.akamaihd.net\/hphotos-ak-xpa1\/t51.2885-19\/10661074_1491498807790266_1996311954_a.jpg",
        "username":"siltamaeki",
        "bio":"",
        "website":"",
        "full_name":"siltamaeki"
        },
        "filter":"Normal",
        "comments":
        {
        "data":[],
        "count":0
        },
        "attribution":null
        },
    ],

    "pagination":
    {
            "next_max_tag_id":"1419870383677955",
            "deprecation_warning":"next_max_id and min_id are deprecated for this endpoint; use min_tag_id and max_tag_id instead",
            "min_tag_id":"1419870385665249",
            "next_min_id":"1419870385665249",
            "next_url":"https:\/\/api.instagram.com\/v1\/tags\/selfie\/media\/recent?client_id=09dec0dbc8fa4a3c8222bd6e6da7a005&max_tag_id=1419870383677955",
            "next_max_id":"1419870383677955"
    },

    "meta":{"code":200}
}

*/
