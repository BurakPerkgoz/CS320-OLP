package olp.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MajorMapperTest {

    @Test
    public void testToDisplayNameArchitectureEnglish() {
        String result = MajorMapper.toDisplayName("mimarlik_ingilizce");
        assertEquals("Mimarlık (EN)", result);
    }

    @Test
    public void testToDisplayNameArchitectureTurkish() {
        String result = MajorMapper.toDisplayName("mimarlik_turkce");
        assertEquals("Mimarlık (TR)", result);
    }

    @Test
    public void testToDisplayNameInteriorArchitecture() {
        String result = MajorMapper.toDisplayName("ic_mimarlik");
        assertEquals("İç Mimarlık ve Çevre Tasarımı", result);
    }

    @Test
    public void testToDisplayNameInternationalFinance() {
        String result = MajorMapper.toDisplayName("uluslararasi_finans");
        assertEquals("Uluslararası Finans", result);
    }

    @Test
    public void testToDisplayNameInternationalBusinessAndTrade() {
        String result = MajorMapper.toDisplayName("uluslararasi_ticaret_ve_isletme");
        assertEquals("Uluslararası İşletmecilik ve Ticaret", result);
    }

    @Test
    public void testToDisplayNameManagementInformationSystems() {
        String result = MajorMapper.toDisplayName("yonetim_bilisim_sistemler");
        assertEquals("Yönetim Bilişim Sistemleri", result);
    }

    @Test
    public void testToDisplayNameBusinessAdministration() {
        String result = MajorMapper.toDisplayName("isletme");
        assertEquals("İşletme", result);
    }

    @Test
    public void testToDisplayNamePilotage() {
        String result = MajorMapper.toDisplayName("pilotaj");
        assertEquals("Pilotaj", result);
    }

    @Test
    public void testToDisplayNameAviation() {
        String result = MajorMapper.toDisplayName("havacilik");
        assertEquals("Havacılık Yönetimi", result);
    }

    @Test
    public void testToDisplayNameLaw() {
        String result = MajorMapper.toDisplayName("hukuk");
        assertEquals("Hukuk", result);
    }

    @Test
    public void testToDisplayNameEconomics() {
        String result = MajorMapper.toDisplayName("ekonomi");
        assertEquals("Ekonomi", result);
    }

    @Test
    public void testToDisplayNameEntrepreneurship() {
        String result = MajorMapper.toDisplayName("girisimcilik");
        assertEquals("Girişimcilik", result);
    }

    @Test
    public void testToDisplayNameIndustrialDesign() {
        String result = MajorMapper.toDisplayName("endustriyel_tasarim");
        assertEquals("Endüstriyel Tasarım", result);
    }

    @Test
    public void testToDisplayNameCommunicationDesign() {
        String result = MajorMapper.toDisplayName("iletisim_tasarim");
        assertEquals("İletişim ve Tasarımı", result);
    }

    @Test
    public void testToDisplayNameComputerEngineering() {
        String result = MajorMapper.toDisplayName("bilgisayar");
        assertEquals("Bilgisayar Mühendisliği", result);
    }

    @Test
    public void testToDisplayNameElectricalEngineering() {
        String result = MajorMapper.toDisplayName("elektrik");
        assertEquals("Elektrik - Elektronik Mühendisliği", result);
    }

    @Test
    public void testToDisplayNameIndustrialEngineering() {
        String result = MajorMapper.toDisplayName("endustri");
        assertEquals("Endüstri Mühendisliği", result);
    }

    @Test
    public void testToDisplayNameCivilEngineering() {
        String result = MajorMapper.toDisplayName("insaat");
        assertEquals("İnşaat Mühendisliği", result);
    }

    @Test
    public void testToDisplayNameMechanicalEngineering() {
        String result = MajorMapper.toDisplayName("makine");
        assertEquals("Makina Mühendisliği", result);
    }

    @Test
    public void testToDisplayNameAIDataEngineering() {
        String result = MajorMapper.toDisplayName("ai");
        assertEquals("Yapay Zeka ve Veri Mühendisliği", result);
    }

    @Test
    public void testToDisplayNamePsychology() {
        String result = MajorMapper.toDisplayName("psikoloji");
        assertEquals("Psikoloji", result);
    }

    @Test
    public void testToDisplayNameInternationalRelations() {
        String result = MajorMapper.toDisplayName("uluslararasi_iliskiler");
        assertEquals("Uluslararası İlişkiler", result);
    }

    @Test
    public void testToDisplayNameAnthropology() {
        String result = MajorMapper.toDisplayName("antropoloji");
        assertEquals("Antropoloji", result);
    }

    @Test
    public void testToDisplayNameGastronomyCulinaryArts() {
        String result = MajorMapper.toDisplayName("gastronomi");
        assertEquals("Gastronomi ve Mutfak Sanatları", result);
    }

    @Test
    public void testToDisplayNameHotelManagement() {
        String result = MajorMapper.toDisplayName("otel_yoneticiligi");
        assertEquals("Otel Yöneticiliği", result);
    }

    @Test
    public void testToDisplayNameUnknownMajor() {
        String result = MajorMapper.toDisplayName("unknown_major");
        assertEquals("unknown_major", result);
    }

    @Test
    public void testToDisplayNameEmptyString() {
        String result = MajorMapper.toDisplayName("");
        assertEquals("", result);
    }

    @Test
    public void testToDisplayNameNull() {
        String result = MajorMapper.toDisplayName(null);
        assertNull(result);
    }
}

