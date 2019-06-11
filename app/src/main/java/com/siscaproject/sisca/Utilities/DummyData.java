package com.siscaproject.sisca.Utilities;

import com.siscaproject.sisca.Model.Asset;
import com.siscaproject.sisca.Model.Location;
import com.siscaproject.sisca.R;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static List<Asset> getListAsset(){
        String loremIpsumText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam ac nulla lacinia mauris facilisis lacinia in ac turpis. Pellentesque suscipit lacus vitae dui dignissim tristique";

        List<Asset> assetList = new ArrayList<>();
        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M001", loremIpsumText, "K101"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M002", loremIpsumText, "K101"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M003", loremIpsumText, "K101"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M004", loremIpsumText, "K101"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M005", loremIpsumText, "K101"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M006", loremIpsumText, "K101"));

        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M011", loremIpsumText, "I203"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M012", loremIpsumText, "I203"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M013", loremIpsumText, "I203"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M014", loremIpsumText, "I203"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M015", loremIpsumText, "I203"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M016", loremIpsumText, "I203"));

        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M021", loremIpsumText, "K102"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M022", loremIpsumText, "K102"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Meja", "M023", loremIpsumText, "K102"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M024", loremIpsumText, "K102"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M025", loremIpsumText, "K102"));
        assetList.add(new Asset(R.drawable.photo_male_1, "Kursi", "M026", loremIpsumText, "K102"));

        return assetList;
    }

    public static List<Location> getListLocation(){
        List<Location> locationList = new ArrayList<>();
        locationList.add(new Location("Ruang kerja 1", "K101", 6));
        locationList.add(new Location("Ruang istirahat", "I203", 6));
        locationList.add(new Location("Ruang kerja 2", "K102", 6));

        return locationList;
    }
}
