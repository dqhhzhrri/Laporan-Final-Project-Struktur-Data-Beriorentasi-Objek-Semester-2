package model;

/**
 * Class Course merepresentasikan 1 node/vertex pada graph.
 *
 * Dalam project Course Prerequisite Planner:
 * - 1 objek Course = 1 mata kuliah.
 * - Course disimpan di AVL Tree berdasarkan kode mata kuliah.
 * - Kode Course juga dipakai sebagai vertex pada Directed Graph.
 *
 * Atribut data selain ID dan nama:
 * 1. sks
 * 2. semester
 * 3. sifat
 * 4. bidangKeahlian
 * 5. musimBuka
 */
public class Course {
    // Kode mata kuliah, digunakan sebagai key utama di AVL Tree dan Graph.
    // Contoh: IT101, IT201, IT801.
    private String code;

    // Nama lengkap mata kuliah.
    private String name;

    // Jumlah SKS mata kuliah.
    private int sks;

    // Semester rekomendasi pengambilan mata kuliah.
    private int semester;

    // Sifat mata kuliah, misalnya Wajib atau Pilihan.
    private String sifat;

    // Bidang atau rumpun keahlian mata kuliah.
    // Contoh: General, Cybersecurity, Cloud/Jaringan.
    private String bidangKeahlian;

    // Informasi musim/semester dibukanya mata kuliah.
    // Contoh: Ganjil, Genap, Keduanya.
    private String musimBuka;

    /**
     * Constructor digunakan untuk membuat objek Course baru.
     *
     * Parameter yang diterima berasal dari dataset yang sudah dibuat.
     */
    public Course(
            String code,
            String name,
            int sks,
            int semester,
            String sifat,
            String bidangKeahlian,
            String musimBuka
    ) {
        this.code = code;
        this.name = name;
        this.sks = sks;
        this.semester = semester;
        this.sifat = sifat;
        this.bidangKeahlian = bidangKeahlian;
        this.musimBuka = musimBuka;
    }

    // Getter code dipakai oleh AVL Tree dan Graph untuk membaca kode mata kuliah.
    public String getCode() {
        return code;
    }

    // Getter name dipakai untuk menampilkan nama mata kuliah kepada user.
    public String getName() {
        return name;
    }

    // Getter sks dipakai untuk menampilkan jumlah SKS mata kuliah.
    public int getSks() {
        return sks;
    }

    // Getter semester dipakai pada fitur rekomendasi berdasarkan semester.
    public int getSemester() {
        return semester;
    }

    // Getter sifat dipakai untuk menampilkan status Wajib/Pilihan.
    public String getSifat() {
        return sifat;
    }

    // Getter bidangKeahlian dipakai untuk menampilkan rumpun keahlian mata kuliah.
    public String getBidangKeahlian() {
        return bidangKeahlian;
    }

    // Getter musimBuka dipakai untuk menampilkan kapan mata kuliah dibuka.
    public String getMusimBuka() {
        return musimBuka;
    }

    /**
     * Menampilkan detail lengkap satu mata kuliah.
     * Method ini dipakai ketika user mencari mata kuliah berdasarkan kode.
     */
    public void displayDetail() {
        System.out.println("Kode              : " + code);
        System.out.println("Nama              : " + name);
        System.out.println("SKS               : " + sks);
        System.out.println("Semester          : " + semester);
        System.out.println("Sifat             : " + sifat);
        System.out.println("Bidang Keahlian   : " + bidangKeahlian);
        System.out.println("Musim Buka        : " + musimBuka);
    }

    /**
     * Mengubah objek Course menjadi String yang rapi.
     * Method ini otomatis dipanggil saat object dicetak dengan System.out.println().
     */
    @Override
    public String toString() {
        return code + " - " + name
                + " | SKS: " + sks
                + " | Semester: " + semester
                + " | " + sifat
                + " | " + bidangKeahlian
                + " | " + musimBuka;
    }
}
