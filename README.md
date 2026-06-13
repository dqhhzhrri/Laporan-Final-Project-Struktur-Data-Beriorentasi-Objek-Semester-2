# Course Prerequisite Planner
**Sistem Rekomendasi Urutan Pengambilan Mata Kuliah Berbasis Struktur Data**


## 1. Judul Project

## 2. Nama Anggota Kelompok
**Departemen Teknologi Informasi - Institut Teknologi Sepuluh Nopember**
* [NRP Anggota 1] - [Nama Anggota 1]
* [NRP Anggota 2] - [Nama Anggota 2]
* [NRP Anggota 3] - [Nama Anggota 3]

## 3. Deskripsi Masalah
Pengisian Formulir Rencana Studi (FRS) adalah proses krusial setiap awal semester yang sangat bergantung pada aturan prasyarat kurikulum. Seiring berjalannya waktu dan pembaruan kurikulum, aturan prasyarat ini membentuk jaringan (Graph) yang semakin kompleks. Masalah fatal sering terjadi di balik layar sistem FRS ketika terdapat circular dependency atau konflik siklus yang tidak disadari (contoh: untuk menyetujui FRS mata kuliah A disyaratkan lulus matakuliah B, sedangkan untuk mengambil matakuliah B harus siyaratkan lulus matakuliah A).

Jika hal ini terjadi, sistem FRS akan mengalami deadlock dan mahasiswa tidak akan pernah bisa mengambil kelas tersebut. Project ini dirancang sebagai mesin validasi backend untuk sistem FRS. Menggunakan struktur `Graph` dan `algoritma Cycle Detection`, sistem ini bertugas mengawasi dan memastikan seluruh aturan prasyarat FRS selalu logis dan bebas konflik. Setelah divalidasi, sistem akan memanfaatkan Topological Sort untuk memberikan panduan (rekomendasi) pengisian FRS yang paling terstruktur, memastikan mahasiswa bisa mengambil mata kuliah secara berurutan tanpa hambatan hingga lulus.

## 4. Dataset
Dataset yang digunakan adalah data kurikulum buatan sendiri yang didasarkan pada susunan mata kuliah Teknologi Informasi.
* **Total Node (Mata Kuliah):** 27
* **Total Edge (Relasi Prasyarat):** 41
* **Atribut Node:** Kode (ID), Nama Mata Kuliah, SKS, Semester Rekomendasi, Sifat (Wajib/Pilihan), Bidang Keahlian, dan Musim Buka (Ganjil/Genap/Keduanya).
* *Detail dataset dapat dilihat pada folder `dataset/dataset.txt` di repository ini.*

## 5. Struktur Graph yang Digunakan
Sistem menggunakan **Directed Graph (Graph Berarah)** yang diimplementasikan secara manual menggunakan **Adjacency List**.
* Arah edge (garis) mengalir dari mata kuliah prasyarat menuju mata kuliah lanjutan (`Prasyarat -> Mata Kuliah Tujuan`).
* Implementasi menggunakan `Map<String, List<String>>` di Java untuk efisiensi alokasi memori.
* Terdapat juga *Reverse Adjacency List* untuk mempercepat pencarian *incoming edges* (prasyarat langsung dari suatu mata kuliah).

## 6. Struktur Tree yang Digunakan
Sistem menggunakan **AVL Tree** yang diimplementasikan secara manual (tanpa *built-in library*).
* Setiap mata kuliah disimpan ke dalam *node* AVL Tree dengan **Kode Mata Kuliah** sebagai kunci pencarian (*key*).
* AVL Tree dipilih untuk menjamin tinggi *tree* selalu seimbang (*self-balancing*), sehingga operasi pencarian, penyisipan, dan penghapusan selalu memiliki waktu eksekusi yang optimal.

## 7. Algoritma yang Digunakan
1.  **Topological Sort (Kahn's Algorithm):** Digunakan untuk mengurutkan mata kuliah dari yang paling dasar hingga tingkat akhir tanpa melanggar aturan prasyarat. Ini adalah algoritma utama untuk fitur rekomendasi pengambilan SKS.
2.  **Depth-First Search (DFS):** Digunakan untuk menelusuri rantai prasyarat dari mata kuliah tingkat atas mundur ke mata kuliah dasar, serta menemukan prasyarat tidak langsung.
3.  **Breadth-First Search (BFS):** Digunakan untuk melihat mata kuliah lanjutan (*neighbors*) apa saja yang "terbuka" dan dapat diambil setelah menyelesaikan satu mata kuliah tertentu secara bertahap.
4.  **Cycle Detection (Deteksi Siklus via DFS):** Digunakan setiap kali ada penambahan relasi baru untuk mencegah terbentuknya prasyarat yang tidak masuk akal (Contoh: A butuh B, tetapi B butuh A).

## 8. Design Decision Log

| No | Keputusan Desain | Alternatif | Alasan Memilih | Risiko / Kelemahan |
|:---|:---|:---|:---|:---|
| 1 | **Menggunakan Adjacency List** | Adjacency Matrix | Graph kurikulum bersifat *sparse* (relasi antar node relatif sedikit). List jauh lebih hemat memori dibandingkan matriks $27 \times 27$. | Mengecek relasi spesifik A -> B memakan waktu $O(V)$ pada list. |
| 2 | **Menggunakan AVL Tree** | Binary Search Tree (BST) Biasa | Mencegah struktur *tree* menjadi *skewed* (seperti *Linked List*) jika kode mata kuliah dimasukkan berurutan (IT101, IT102, dst). | Logika implementasi rotasi AVL lebih kompleks dibanding BST. |
| 3 | **Membuat Reverse Adjacency List** | Hanya Adjacency List biasa | Mempercepat fitur "Tampilkan Prasyarat" menjadi $O(1)$ untuk melihat *indegree*, karena *list* asli mengarah ke *outdegree*. | Penggunaan memori meningkat dua kali lipat untuk struktur Graph. |
| 4 | **Menggunakan Kahn's Algorithm (Topological Sort)** | DFS Post-order Reverse | Kahn's Algorithm dengan Queue sangat intuitif untuk dikelompokkan berdasarkan parameter Semester. | Bergantung penuh pada penghitungan *indegree* di awal proses. |
| 5 | **Menggunakan `LinkedHashMap` pada Graph** | `HashMap` biasa | Menjaga urutan iterasi Graph sesuai dengan urutan input dataset (*insertion order*). | Performa sedikit lebih lambat dari HashMap standar. |

## 9. Tracing Manual
*(Lihat lampiran file `tracing.pdf` di folder `docs/` untuk visualisasi lengkap. Berikut adalah ringkasan tracing casenya):*

**Tracing Proses Tree (AVL Right-Right Case):**
1. Insert `IT101`. Root = `IT101` (Balance Factor = 0).
2. Insert `IT102`. Posisi di kanan `IT101`. Root = `IT101` (BF = -1).
3. Insert `IT103`. Posisi di kanan `IT102`. Root = `IT101` (BF = -2) -> **Tidak seimbang**.
4. Terjadi **Left Rotation** pada node `IT101`. `IT102` naik menjadi Root baru, `IT101` menjadi anak kiri, `IT103` menjadi anak kanan. Status AVL kembali seimbang (BF = 0).

**Tracing Algoritma Graph (Topological Sort):**
Asumsi Graph kecil: `IT102 -> IT201`, `IT103 -> IT201`, `IT201 -> IT203`.
1. Hitung *indegree*: `IT102` (0), `IT103` (0), `IT201` (2), `IT203` (1).
2. Masukkan node dengan indegree 0 ke Queue: `[IT102, IT103]`.
3. Pop `IT102`, masukkan ke `Result`. Kurangi indegree `IT201` menjadi (1).
4. Pop `IT103`, masukkan ke `Result`. Kurangi indegree `IT201` menjadi (0).
5. Karena `IT201` sekarang 0, masukkan `IT201` ke Queue.
6. Pop `IT201`, kurangi indegree `IT203` menjadi (0). Masukkan `IT203` ke Queue.
7. Pop `IT203`. Queue kosong. Proses selesai. Urutan valid didapatkan.

## 10. Screenshot Hasil Program
*(Ganti placeholder di bawah ini dengan screenshot terminal dari program yang dijalankan)*

* **Menu Utama & Tampil Semua Mata Kuliah**
    ![Menu Utama](link-gambar-menu-utama.png)
* **Fitur Rekomendasi (Topological Sort per Semester)**
    ![Topological Sort](link-gambar-topo-sort.png)
* **Fitur Deteksi Siklus (Cycle Detection)**
    ![Cycle Detection](link-gambar-cycle.png)

## 11. Analisis Kompleksitas

| Operasi | Struktur / Algoritma | Kompleksitas Waktu | Alasan |
|:---|:---|:---|:---|
| **Insert / Search Data** | AVL Tree | $O(\log N)$ | Karena AVL Tree selalu menyeimbangkan diri (ketinggian maksimal adalah logaritma dari total node $N$), menelusuri ke kiri/kanan selalu memotong area pencarian separuhnya. |
| **Pencarian Prasyarat Langsung** | Reverse Adjacency List | $O(1)$ | Mengambil *value* (tetangga) dari sebuah *key* pada `Map` (Hash) membutuhkan waktu konstan. |
| **Menampilkan Rantai DFS** | Depth-First Search | $O(V + E)$ | Algoritma menelusuri seluruh Vertex ($V$) dan Edge ($E$) yang relevan di sepanjang jalur untuk menemukan prasyarat. |
| **Rekomendasi Urutan** | Topological Sort (Kahn's) | $O(V + E)$ | Algoritma membaca semua *vertex* untuk inisialisasi *Queue* $O(V)$, lalu memeriksa setiap tetangga *edge* untuk mengurangi *indegree* $O(E)$. |

## 12. What-if Analysis
...

## 13. Kesimpulan
Implementasi gabungan struktur data AVL Tree dan Directed Graph terbukti sangat efektif untuk memecahkan masalah penyusunan jadwal dan analisis prasyarat kurikulum. AVL Tree memastikan manipulasi dan akses data entitas mata kuliah berlangsung sangat cepat $O(\log n)$, sementara Graph memungkinkan pemetaan dan analisis relasi kompleks (Topological Sort dan Deteksi Siklus) berjalan secara dinamis. Project ini telah berhasil memenuhi seluruh spesifikasi fungsional dan teknis yang disyaratkan.
