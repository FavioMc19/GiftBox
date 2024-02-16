package net.kokoricraft.giftbox.enums;

public enum BoxSkins {
    DEFAULT("http://textures.minecraft.net/texture/173b074c966c79d28ffe741464822feff423c5c6c3ef633747c2bda3ba14297e"
            , "http://textures.minecraft.net/texture/2c185324ae42c4908d930182a0c6874a18ed878f2311c3ee35c0b134abb62de4"),
    GOLDEN("", "")
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
