package net.kokoricraft.giftbox.enums;

public enum BoxSkins {
    DEFAULT("https://textures.minecraft.net/texture/173b074c966c79d28ffe741464822feff423c5c6c3ef633747c2bda3ba14297e"
            , "https://textures.minecraft.net/texture/2c185324ae42c4908d930182a0c6874a18ed878f2311c3ee35c0b134abb62de4"),
    GOLDEN("", ""),
    NORMAL("https://textures.minecraft.net/texture/bd704d00845e41d13195613c1889fb4e3c773b75198da94bdcef44f60028f9a5"
    , "https://textures.minecraft.net/texture/6bd1eb477dd0caa7f75d01ba4d2028f16d433b0f3ff46ae568d3b1aa1784b697"),
    RARE("https://textures.minecraft.net/texture/1feafb119721dc650f5fc6004814229c2abc9232dbce937aaf1ab5ddd74edfc3",
            "https://textures.minecraft.net/texture/1f15f2b8463374011286612ee1252adeb1fc9ab961865a5293ebd35a5dd2907b"),
    EPIC("https://textures.minecraft.net/texture/627716515fd61c62e64eba6542800109090c08eace5180f2fdece112112513a5",
            "https://textures.minecraft.net/texture/bd6af9d2cc1a28391c92df1d1d0a5729ca303c6684599d47de11e2633572e2bf")
    ;

    private final String body;
    private final String lid;
    BoxSkins(String body, String lid){
        this.body = body;
        this.lid = lid;
    }

    public String getBody(){
        return body;
    }

    public String getLid(){
        return lid;
    }
}
