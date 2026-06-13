# Course Prerequisite Planner

Project Struktur Data untuk membuat sistem rekomendasi urutan pengambilan mata kuliah berdasarkan relasi prasyarat.

## Dataset

Dataset menggunakan sheet `MK IT ITS` dari file `Dataset.xlsx`.

- Jumlah node / mata kuliah: 27
- Jumlah edge / relasi prasyarat: 41
- Graph: directed graph
- Tree: AVL Tree manual
- Graph storage: adjacency list manual

## Atribut Course

1. Kode
2. Nama Mata Kuliah
3. SKS
4. Semester
5. Sifat
6. Bidang Keahlian
7. Musim Buka

Atribut selain ID dan nama: SKS, Semester, Sifat, Bidang Keahlian, Musim Buka.

## Struktur Folder

```text
FP_Strukdat_CoursePrerequisitePlanner_DatasetITS/
├── src/
│   ├── Main.java
│   ├── model/
│   │   └── Course.java
│   ├── tree/
│   │   ├── AVLNode.java
│   │   └── AVLTree.java
│   ├── graph/
│   │   └── CourseGraph.java
│   └── data/
│       └── CourseSeeder.java
├── dataset/
│   ├── dataset.txt
│   └── prerequisites.txt
└── README.md
```

## Cara Compile dan Run

Jalankan dari root folder project:

```bash
javac -d out src/Main.java src/model/Course.java src/tree/AVLNode.java src/tree/AVLTree.java src/graph/CourseGraph.java src/data/CourseSeeder.java
java -cp out Main
```

## Fitur

1. Tampilkan semua mata kuliah
2. Cari mata kuliah berdasarkan kode
3. Cari mata kuliah berdasarkan prefix kode/nama
4. Tampilkan prasyarat langsung
5. Tampilkan semua prasyarat langsung dan tidak langsung
6. Tampilkan rantai prasyarat DFS
7. BFS dari mata kuliah tertentu
8. Rekomendasi urutan pengambilan menggunakan Topological Sort
9. Rekomendasi berdasarkan semester
10. Deteksi siklus prasyarat
11. Simulasi penambahan mata kuliah baru
12. Tambah relasi prasyarat
13. Hapus relasi prasyarat
14. Fitur gabungan AVL Tree dan Graph
15. Tampilkan graph adjacency list
16. Hapus mata kuliah dari AVL Tree
17. Tampilkan ringkasan dataset

## Algoritma Graph

- DFS: menampilkan rantai prasyarat dan semua prasyarat tidak langsung.
- BFS: menelusuri mata kuliah lanjutan dari satu mata kuliah awal.
- Topological Sort: membuat rekomendasi urutan pengambilan mata kuliah.
- Cycle Detection: mendeteksi konflik kurikulum, misalnya A membutuhkan B tetapi B juga membutuhkan A.
