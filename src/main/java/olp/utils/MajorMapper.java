package olp.utils;

import java.util.Map;

public class MajorMapper {

    private static final Map<String, String> MAJOR_MAP = Map.ofEntries(
        Map.entry("mimarlik_ingilizce", "Mimarlık (EN)"),
        Map.entry("mimarlik_turkce", "Mimarlık (TR)"),
        Map.entry("ic_mimarlik", "İç Mimarlık ve Çevre Tasarımı"),
        Map.entry("uluslararasi_finans", "Uluslararası Finans"),
        Map.entry("uluslararasi_ticaret_ve_isletme", "Uluslararası İşletmecilik ve Ticaret"),
        Map.entry("yonetim_bilisim_sistemler", "Yönetim Bilişim Sistemleri"),
        Map.entry("isletme", "İşletme"),
        Map.entry("pilotaj", "Pilotaj"),
        Map.entry("havacilik", "Havacılık Yönetimi"),
        Map.entry("hukuk", "Hukuk"),
        Map.entry("ekonomi", "Ekonomi"),
        Map.entry("girisimcilik", "Girişimcilik"),
        Map.entry("endustriyel_tasarim", "Endüstriyel Tasarım"),
        Map.entry("iletisim_tasarim", "İletişim ve Tasarımı"),
        Map.entry("bilgisayar", "Bilgisayar Mühendisliği"),
        Map.entry("elektrik", "Elektrik - Elektronik Mühendisliği"),
        Map.entry("endustri", "Endüstri Mühendisliği"),
        Map.entry("insaat", "İnşaat Mühendisliği"),
        Map.entry("makine", "Makina Mühendisliği"),
        Map.entry("ai", "Yapay Zeka ve Veri Mühendisliği"),
        Map.entry("psikoloji", "Psikoloji"),
        Map.entry("uluslararasi_iliskiler", "Uluslararası İlişkiler"),
        Map.entry("antropoloji", "Antropoloji"),
        Map.entry("gastronomi", "Gastronomi ve Mutfak Sanatları"),
        Map.entry("otel_yoneticiligi", "Otel Yöneticiliği")
    );

    public static String toDisplayName(String key) {
        return MAJOR_MAP.getOrDefault(key, key);
    }
}