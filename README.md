# TaskTracker

TaskTracker, kullanıcıların günlük görevlerini oluşturabildiği, düzenleyebildiği, silebildiği ve tamamlanma durumlarını takip edebildiği basit bir Android To-Do uygulamasıdır.

Uygulama Kotlin ve Jetpack Compose kullanılarak geliştirilmiştir. Görevler Room veritabanında kalıcı olarak saklanır. Uygulamanın ilk kullanımında JSONPlaceholder API üzerinden örnek görevler alınarak yerel veritabanına kaydedilir.

## Özellikler

- Görevleri listeleme
- Yeni görev ekleme
- Var olan görevi düzenleme
- Görev silme
- Görevleri tamamlandı / tamamlanmadı olarak işaretleme
- Görev oluşturulma tarihini gösterme
- Room ile kalıcı veri saklama
- Retrofit ile uzak API'den örnek görevleri alma
- API verilerini yalnızca ilk kullanımda yükleme
- Network hata durumunu kullanıcıya gösterme
- Liste boş olduğunda empty state gösterme
- Görevleri oluşturulma tarihine göre sıralama
    - En Yeni
    - En Eski
- Dark Mode desteği
- Uygulama içerisinden manuel Dark Mode değiştirme
- MVVM mimarisi
- Coroutines + Flow / StateFlow ile reaktif veri akışı
- Navigation Compose ile ekranlar arası geçiş
- ViewModel katmanı için Unit Testler

## Kullanılan Teknolojiler

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Room
- Retrofit
- Gson
- Kotlin Coroutines
- Flow
- StateFlow
- Navigation Compose
- SharedPreferences
- JUnit
- kotlinx-coroutines-test
- Git
- GitHub

## Mimari

Projede MVVM mimarisi kullanılmıştır.

Genel veri akışı:

```text
Jetpack Compose UI
        ↓
     ViewModel
        ↓
    Repository
      ↙     ↘
   Room    Retrofit
```

UI katmanı doğrudan Room veya Retrofit ile iletişim kurmaz.

ViewModel, Repository üzerinden veri işlemlerini gerçekleştirir. Repository ise yerel veri kaynağı olan Room ve uzak veri kaynağı olan Retrofit arasında bağlantı sağlar.

Bu yapı:

- Kodun katmanlara ayrılmasını
- Kod okunabilirliğini
- Bakım kolaylığını
- Test edilebilirliği
- UI ve veri katmanlarının birbirinden ayrılmasını

sağlamak amacıyla tercih edilmiştir.

## Proje Yapısı

```text
com.kafein.tasktracker
│
├── data
│   ├── local
│   │   ├── TaskEntity
│   │   ├── TaskDao
│   │   ├── TaskDatabase
│   │   └── InitialDataPreferences
│   │
│   └── remote
│       ├── TodoDto
│       ├── TodoApi
│       ├── RetrofitClient
│       └── TodoMapper
│
├── repository
│   ├── TaskRepository
│   └── TaskRepositoryContract
│
├── ui
│   ├── navigation
│   │   └── AppNavigation
│   │
│   ├── screens
│   │   ├── TaskListScreen
│   │   └── AddEditTaskScreen
│   │
│   └── theme
│
├── viewmodel
│   ├── TaskViewModel
│   └── TaskViewModelFactory
│
└── MainActivity
```

## Yerel Veri Saklama

Görevlerin kalıcı olarak saklanması için Room kullanılmıştır.

Her görev temel olarak aşağıdaki alanları içerir:

```text
id
title
isCompleted
createdAt
```

Uygulama kapatılıp tekrar açıldığında görevler kaybolmaz.

Görev ekleme, güncelleme, silme ve listeleme işlemleri `TaskDao` üzerinden gerçekleştirilir.

Room'dan gelen görev listesi `Flow` olarak takip edildiği için veritabanında meydana gelen değişiklikler otomatik olarak UI katmanına yansıtılır.

## Network Katmanı

Uzak API işlemleri için Retrofit kullanılmıştır.

Kullanılan örnek API:

https://jsonplaceholder.typicode.com/todos

API'den gelen JSON verileri `TodoDto` modeline dönüştürülür.

Ardından `TodoMapper` yardımıyla:

```text
TodoDto
   ↓
TaskEntity
```

dönüşümü gerçekleştirilerek görevler Room veritabanına kaydedilir.

İlk kullanımda API'den gelen ilk 10 görev örnek veri olarak uygulamaya eklenir.

Başlangıç verilerinin daha önce yüklenip yüklenmediği `SharedPreferences` ile takip edilir. Böylece kullanıcı bütün görevleri silse bile uygulama tekrar açıldığında API görevleri otomatik olarak yeniden eklenmez.

## ViewModel ve State Yönetimi

UI durumlarının yönetimi `TaskViewModel` üzerinden gerçekleştirilir.

Görev listesi `StateFlow` ile Compose ekranına aktarılır.

Temel akış:

```text
Room
 ↓
Flow
 ↓
Repository
 ↓
StateFlow
 ↓
ViewModel
 ↓
Jetpack Compose
```

Room içerisindeki bir görev değiştiğinde Compose ekranı otomatik olarak yeniden güncellenir.

ViewModel üzerinden aşağıdaki işlemler gerçekleştirilmektedir:

- Görev ekleme
- Görev düzenleme
- Görev silme
- Görev tamamlanma durumunu değiştirme
- Başlangıç API verilerini yükleme
- Network hata durumunu yönetme
- Görevleri tarihe göre sıralama

## Navigation

Ekranlar arası geçiş için Navigation Compose kullanılmıştır.

Uygulamada temel olarak iki ekran bulunmaktadır:

### Görev Listesi

Görevlerin görüntülendiği ana ekrandır.

Buradan:

- Yeni görev eklenebilir
- Görev düzenlenebilir
- Görev silinebilir
- Checkbox ile tamamlanma durumu değiştirilebilir
- En Yeni / En Eski sıralaması yapılabilir
- Dark Mode değiştirilebilir

### Görev Ekle / Düzenle

Yeni görev oluşturmak ve mevcut görevlerin başlığını düzenlemek için aynı ekran yeniden kullanılmaktadır.

Bu sayede aynı işlev için iki ayrı UI oluşturmak yerine tekrar kullanılabilir bir yapı tercih edilmiştir.

## Dark Mode

Uygulama Material 3 tema sistemi kullanmaktadır.

Sistem temasına göre otomatik Dark Mode desteğinin yanında, kullanıcı ana ekrandaki Switch üzerinden manuel olarak:

```text
Light Mode ↔ Dark Mode
```

geçişi yapabilir.

## Görev Sıralama

Görevler oluşturulma tarihine göre sıralanabilir.

Desteklenen seçenekler:

- En Yeni
- En Eski

Sıralama işlemi ViewModel tarafında gerçekleştirilmektedir.

Room'daki verinin kendisi değiştirilmeden yalnızca kullanıcıya gösterilen görev sırası değiştirilmektedir.

## Unit Testler

ViewModel katmanı için 3 adet Unit Test bulunmaktadır.

Test edilen senaryolar:

1. Görev eklenirken başındaki ve sonundaki boşlukların temizlenmesi
2. Boş başlıklı görevin eklenmemesi
3. Görev tamamlanma durumunun doğru şekilde değiştirilmesi

Testlerde gerçek Room veya Retrofit kullanılmamaktadır.

Bunun yerine `TaskRepositoryContract` implementasyonu olan bir `FakeTaskRepository` kullanılmaktadır.

Bu sayede ViewModel bağımsız şekilde test edilebilmektedir.

Testleri terminal üzerinden çalıştırmak için:

```bash
./gradlew testDebugUnitTest
```

Başarılı durumda:

```text
BUILD SUCCESSFUL
```

çıktısı alınır.

## Projeyi Çalıştırma

Projeyi çalıştırmak için:

1. Repository'yi bilgisayarınıza klonlayın.
2. Android Studio ile projeyi açın.
3. Gradle Sync işleminin tamamlanmasını bekleyin.
4. Android Emulator veya fiziksel Android cihaz seçin.
5. Run butonuna basarak uygulamayı çalıştırın.

## Teknik Tercihler

### Neden Kotlin?

Android geliştirme için modern dil desteği, null safety ve coroutine desteği sağladığı için tercih edilmiştir.

### Neden Jetpack Compose?

XML tabanlı View sistemine göre daha modern ve deklaratif bir UI geliştirme yaklaşımı sunduğu için kullanılmıştır.

State değişikliklerinin UI'a yansıtılması daha kolay ve daha okunabilir şekilde gerçekleştirilebilmektedir.

### Neden Room?

Android içerisinde SQLite üzerinde güvenli ve sürdürülebilir bir abstraction sağladığı için tercih edilmiştir.

DAO yapısı sayesinde veritabanı işlemleri uygulamanın diğer katmanlarından ayrılmıştır.

### Neden Retrofit?

REST API işlemleri için Android ekosisteminde yaygın, sade ve güçlü bir çözüm olduğu için kullanılmıştır.

JSON verilerinin Kotlin modellerine dönüştürülmesini kolaylaştırmaktadır.

### Neden MVVM?

UI, state yönetimi ve veri işlemlerini birbirinden ayırmak için tercih edilmiştir.

Ayrıca ViewModel katmanının bağımsız olarak Unit Test yazılabilmesini kolaylaştırmaktadır.

### Neden Repository?

ViewModel'in doğrudan Room veya Retrofit ile iletişim kurmasını engellemek için Repository katmanı kullanılmıştır.

Bu sayede veri kaynağının nereden geldiği ViewModel açısından önemli değildir.

### Neden Flow / StateFlow?

Room verilerinin reaktif olarak takip edilmesini ve veri değişikliklerinin Compose UI'a otomatik şekilde aktarılmasını sağlamak için kullanılmıştır.

### Neden SharedPreferences?

API başlangıç verilerinin daha önce yüklenip yüklenmediği gibi küçük bir boolean değeri saklamak için kullanılmıştır.

Bu kadar küçük bir veri için ayrı bir veritabanı yapısı kullanmak yerine basit bir kalıcı saklama yöntemi tercih edilmiştir.

## Yapılabilecek İyileştirmeler

Daha fazla geliştirme süresi olması durumunda aşağıdaki özellikler eklenebilir:

- Görev kategorileri
- Tamamlandı / tamamlanmadı filtresi
- Swipe-to-delete
- Görev silmeden önce confirmation dialog
- Snackbar ile silme işlemini geri alma
- Daha gelişmiş animasyonlar
- Dark Mode tercihinin DataStore ile kalıcı saklanması
- Daha kapsamlı Repository testleri
- Compose UI testleri
- Dependency Injection için Hilt kullanımı
- Görev detay ekranı
- Arama özelliği
- Daha gelişmiş tarih ve zaman yönetimi

## Bilinen Eksikler

Uygulamanın temel görev takip senaryoları çalışmaktadır.

Manuel Dark Mode seçimi uygulama çalışırken korunmaktadır ancak kullanıcı tercihinin kalıcı olarak saklanması için DataStore kullanılmamıştır.

API yalnızca örnek başlangıç verileri sağlamak için kullanılmaktadır. Uygulamada oluşturulan, düzenlenen ve silinen görevler yerel Room veritabanında tutulmaktadır.

## Versiyon Kontrolü

Proje Git kullanılarak geliştirilmiştir.

Geliştirme sürecinde özellikler tek bir büyük commit yerine anlamlı ve düzenli commitler halinde GitHub'a gönderilmiştir.

Örnek commit konuları:

```text
Initial Android project setup
Add project dependencies
Add Room database layer
Add repository and task ViewModel
Add Retrofit API layer
Connect API data flow and ViewModel
Implement task list screen
Add task creation flow and navigation
Add task editing flow
Add task deletion
Persist initial API load state
Add ViewModel unit tests
Add task sorting by creation date
Add manual dark mode toggle
```

## Sonuç

TaskTracker projesi ile modern Android geliştirmede kullanılan temel yapıların birlikte kullanılması amaçlanmıştır.

Projede:

```text
Kotlin
Jetpack Compose
MVVM
Room
Retrofit
Coroutines
Flow
StateFlow
Navigation Compose
Unit Testing
Git
```

teknolojileri birlikte kullanılarak basit fakat katmanlı ve geliştirilebilir bir Android uygulaması oluşturulmuştur.