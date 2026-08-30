# Odgovor Android strane na ugovor o sinhronizaciji

Odgovor na dokument *Ugovor o sinhronizaciji: Android (CS330) ↔ Web (IT354)*.

**Projekat:** `project250` (Kotlin, Jetpack Compose, Firebase Realtime Database)
**Datum:** 30.08.2026.

---

## 1. Odgovor na tačku 7 — polje `stability`

**Odgovor je (b): polje je zaostatak, nije u upotrebi.**

Provereno je u trenutnom izvornom kodu i u sve tri arhivirane verzije projekta
(`CS330-PZ-Nikola-Zivkovic-6090.zip`, `project250.zip`,
`CS330-NIKOLA-ZIVKOVIC-6090-FINALNA-VERZIJA-PROJEKTA.zip`). Niz `stability` ne
postoji nigde — ni u `CarModel`, ni u formi, ni u bilo kom `.kt` ili `.json`
fajlu. Nijedna verzija Android aplikacije to polje nikada nije upisala ni čitala.

Vrednost je u svih 7 zapisa `0`, što se poklapa sa hipotezom da je nastala
jednokratno (ručna izmena u Firebase konzoli ili rani prototip koji nije ušao u
predaju), a ne iz aplikacije.

**Predlog:** web strana može jednokratno obrisati tih 7 vrednosti. Android
strana nema šta da ukloni iz modela — polja tamo nema.

> Napomena: i posle brisanja, `updateChildren` iz tačke 3.2 ostaje na snazi na
> Android strani. Ako web strana kasnije uvede neko novo polje, Android ga više
> neće brisati pri izmeni oglasa.

---

## 2. Šta je urađeno u Android aplikaciji

Kontrolna lista iz tačke 10, sa konkretnim izmenama:

| Stavka | Status | Gde |
|---|---|---|
| Model usklađen sa tačkom 2 | urađeno, uz jedno odstupanje (vidi 3.1) | `domain/CarModel.kt` |
| Svi članovi imaju podrazumevanu vrednost, nema `@ThrowOnExtraProperties` | već je bilo tako | `domain/CarModel.kt` |
| Numerički unos preko `.toIntOrNull() ?: 0` | urađeno | `ui/feature/addcar/AddCarScreen.kt` |
| Izmena koristi `updateChildren` | **ispravljeno** | `viewModel/CarViewModel.kt` |
| Ključ se ne upisuje kao polje `id` | već je bilo tako (`@get:Exclude`) | `domain/CarModel.kt` |
| `userId` je stvarni UID | urađeno + ispravljen bag (vidi 2.2) | `viewModel/CarViewModel.kt` |
| Dodato `"Plin"` | urađeno | `ui/feature/addcar/AddCarScreen.kt` |
| Dodato polje `phone` (model + forma + detalji) | urađeno | 3 fajla, vidi 2.3 |
| Odgovor na pitanje o `stability` | vidi tačku 1 ovog dokumenta | — |
| Forma validira unos, nema praznih enumeracija | urađeno | `ui/feature/addcar/AddCarScreen.kt` |
| Provereno pod pravilima iz `database.rules.json` | **nije moguće** — vidi tačku 4 | — |

### 2.1. `updateChildren` umesto `setValue` (tačka 3.2)

`CarViewModel.updateCar()` je do sada radio `setValue(carWithUser)` nad celim
objektom. Pošto Android model nije poznavao `phone`, svaka izmena oglasa sa
telefona bi obrisala i `phone` i `stability`.

Sada se šalje mapa samo onih polja koja forma zaista menja:

```kotlin
val updates = mapOf(
    "title" to updatedCar.title,
    "price" to updatedCar.priceInt,
    // ... ostala polja forme ...
    "phone" to updatedCar.phone?.takeIf { it.isNotBlank() }
)
carsRef().child(carId).updateChildren(updates)
```

Sve numeričke vrednosti idu kroz `*Int` getere, tako da u bazu uvek ide broj,
nikada string (tačka 3.1).

### 2.2. Usput ispravljen bag: izmena je prepisivala vlasnika

Stari `updateCar` je radio `updatedCar.copy(userId = currentUid)`, uz komentar da
je to bezbedno jer izmenu ionako radi samo vlasnik. To nije tačno — aplikacija
ima administratorski ekran (vidi tačku 3.2 ovog dokumenta) sa kog se menjaju tuđi
oglasi. Takva izmena bi tiho prebacila vlasništvo nad oglasom na administratora.

Novi `updateChildren` uopšte ne dira `userId`. Vlasnik oglasa se izmenom više ne
menja, što je u skladu sa tačkom 4 ugovora.

### 2.3. Polje `phone` (tačka 6)

- `domain/CarModel.kt` — dodato `val phone: String? = null`
- `ui/feature/addcar/AddCarScreen.kt` — opciono polje u formi, ograničeno na 30
  karaktera, tastatura tipa `Phone`; prazno polje se upisuje kao `null` (čvor se
  briše), ne kao prazan string
- `ui/feature/detail/DetailContact.kt` — **nova komponenta**; prikazuje broj i
  dugme *Call* koje otvara `Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))`.
  Ako polja nema, prikazuje se poruka da prodavac nije ostavio kontakt
- `ui/feature/detail/DetailScreen.kt` — komponenta ubačena između specifikacija i
  opisa

Korišćen je `ACTION_DIAL`, ne `ACTION_CALL` — samo otvara pozivnik sa upisanim
brojem, pa nije potrebna `CALL_PHONE` dozvola.

### 2.4. Validacija forme (tačke 3.4 i 9)

Dugme za čuvanje sada odbija unos pre bilo kakvog upisa u bazu, sa konkretnom
porukom. Uslovi su namerno postavljeni tako da odgovaraju predloženim
sigurnosnim pravilima:

| Polje | Uslov |
|---|---|
| `title` | 3–80 karaktera (posle `trim()`) |
| `price` | broj > 0 |
| `description` | najviše 1000 karaktera |
| `picUrl` | počinje sa `http://` ili `https://` |
| `categoryId` | marka mora biti izabrana |
| `productionYear` | 1950–2100 |
| `mileage` | ≥ 0 |
| `fuelType` | mora biti jedna od 5 dozvoljenih vrednosti |
| `transmission` | mora biti jedna od 2 dozvoljene vrednosti |
| `engineVolume` | ≥ 0 |
| `enginePower` | > 0 |
| `highestSpeed` | > 0 |
| `seats` | 1–9 |
| `phone` | najviše 30 karaktera, opciono |

Prazan `fuelType` / `transmission` više ne može da prođe kroz formu. Zapis
`-OtBLbzuUBeqLixpnyku` iz tačke 8 je nastao pre ove izmene; može se popraviti sa
bilo koje strane.

### 2.5. Dodatno: lista se sada osvežava uživo

Ovo nije bilo na listi, ali bez toga test iz tačke 11 („na telefonu se lista mora
ažurirati sama") ne bi prošao.

`fetchCars()` je koristio `addListenerForSingleValueEvent` — jednokratno čitanje.
Izmena napravljena na web-u se na telefonu videla tek posle ponovnog ulaska u
ekran. Sada:

- `Cars` se čita preko `addValueEventListener` (trajna pretplata),
- listeneri se registruju najviše jednom i odjavljuju u `onCleared()` (ranije se
  `fetchUserCars()` pozivao pri svakom ulasku na ekran i kačio novi listener
  svaki put),
- ekran detalja se više ne oslanja samo na `Parcelable` snimak prosleđen kroz
  navigaciju, nego uzima aktuelnu verziju oglasa iz liste uživo, pa se izmena sa
  web-a vidi i bez izlaska sa ekrana,
- forma za izmenu se popunjava tek kad traženi oglas stigne, i to samo jednom, da
  osvežavanje ne bi pregazilo ono što korisnik u tom trenutku kuca.

---

## 3. Otvorene tačke i neslaganja (tačka 13)

### 3.1. Tipovi u modelu — odstupanje koje predlažem da ostane

Ugovor traži `Int` za numerička polja. Android model ih drži kao `Any?` uz
gettere `priceInt`, `categoryIdInt` itd. koji prihvataju i broj i string.

Razlog: `getValue(CarModel::class.java)` sa `Int` poljem **puca na celom
snapshot-u** ako makar jedan zapis u bazi ima string u numeričkom polju — dakle
ne preskoči taj oglas, nego obori učitavanje cele liste. `Any?` je strogo
tolerantniji.

Ovo ne narušava ugovor: **upis je i dalje strogo numerički** — i `addCar` i
`updateCar` šalju `Int` vrednosti dobijene iz `*Int` getera. `Any?` utiče samo na
čitanje.

Ako web strana ipak insistira na `Int` u modelu, promena je izvodljiva, ali tek
pošto se potvrdi da u bazi nema nijednog stringa u numeričkom polju — i ostaje
trajno osetljiva na jedan loš zapis.

### 3.2. Administratorski ekran je u sukobu sa pravilima iz tačke 9

Aplikacija ima ekran *Manage Cars (Admin)*, dostupan nalogu sa e-poštom
`admin@admin.com` (`ui/feature/profile/ProfileScreen.kt`). Sa njega se menja i
briše **bilo koji** oglas, ne samo sopstveni.

Predložena pravila iz tačke 9 to odbijaju, a odbijanje se na Android strani vidi
kao tiho neuspešno čuvanje. Tri opcije:

- **(a)** ukloniti administratorski ekran — najjednostavnije, i u skladu sa
  tačkom 4 ugovora;
- **(b)** zadržati ga, uz svest da će posle primene pravila prestati da radi;
- **(c)** pravila proširiti izuzetkom za konkretan admin UID.

Moj predlog je **(a)**, ali odluka menja i jednu i drugu stranu, pa je ostavljam
za dogovor.

### 3.3. Pravila moraju pokriti i čvor `Users`, ne samo `Cars` i `Category`

Ovo je najverovatnije previd u predlogu pravila — web aplikacija ne zna za ovaj
čvor.

Android aplikacija ima listu omiljenih oglasa i upisuje je u:

```
Users/{uid}/favorites/{carId} = true
```

(`CarViewModel.toggleFavorite`, `fetchFavorites`). Ako `database.rules.json`
zabrani upis svuda osim pod `Cars`, omiljeni oglasi će prestati da rade — i to
tiho, bez ikakve poruke korisniku.

**Potrebno pravilo:** pod `Users/$uid` dozvoliti čitanje i upis kada je
`auth.uid === $uid`.

Za red veličine: ovo je jedini čvor u Realtime Database-u van `Cars` i
`Category` koji Android koristi.

### 3.4. Profil korisnika je u Firestore-u, ne u Realtime Database-u

Radi potpune slike: ekran za izmenu profila (`EditProfileScreen.kt`) čuva ime,
telefon i sliku profila u **Cloud Firestore** kolekciji `users/{uid}`, što je
sasvim odvojena baza od Realtime Database-a i nije predmet ovog ugovora. Pominjem
jer se polje zove `phoneNumber` i lako se pomeša sa novim `phone` na oglasu — to
su dve različite stvari:

- `users/{uid}.phoneNumber` (Firestore) — telefon **korisnika**, na profilu
- `Cars/{id}.phone` (RTDB) — telefon **na konkretnom oglasu**, iz ugovora

Ako želimo, forma za nov oglas može ponuditi profilni broj kao podrazumevanu
vrednost. Nije urađeno jer nije bilo u zahtevu.

### 3.5. Demo oglasi sa `userId: "admin"` (upozorenje iz tačke 9)

Slažem se sa opisom problema. Predlog: pre primene pravila prepisati tih 20
zapisa na stvarni UID `8U6WS7cFmxOBDMYOa8d9hCehsjr2`, da bi demo podaci ostali
upotrebljivi za demonstraciju izmene i brisanja na obe platforme. Alternativa
(ostaviti ih trajno nepromenljivim) znači da se na demonstraciji mora prvo
napraviti nov oglas da bi se pokazalo bilo šta osim čitanja.

Ovih 20 zapisa su verovatno i istih 20 od 21 zapisa sa suvišnim poljem `id` iz
tačke 8 — pa se obe stvari mogu srediti u istom prolazu.

---

## 4. Šta nije provereno

**Aplikacija nije kompajlirana.** Na ovoj mašini nema instaliranog Android SDK-a
(`ANDROID_HOME` nije postavljen, nema `local.properties`, nema
`AppData\Local\Android\Sdk`), pa `gradlew assembleDebug` ne može da se pokrene.
Izmene su pisane i pregledane ručno, ali ih treba prevesti i pokrenuti pre
predaje.

Posledično, nije odrađen ni test iz tačke 11 ni provera ponašanja pod pravilima
iz `database.rules.json`.

Kad SDK bude dostupan, redosled provere:

1. `gradlew assembleDebug` — prevođenje
2. dvosmerni test iz tačke 11, sa istim nalogom na obe platforme
3. posebno proveriti da posle izmene sa telefona polje `phone` i dalje postoji u
   zapisu (dokaz da `updateChildren` radi)
4. tek onda primeniti pravila, i ponoviti 2 i 3

---

## 5. Izmenjeni fajlovi

```
project250/app/src/main/java/com/zivkovic/project250/
├── domain/CarModel.kt                          + phone
├── viewModel/CarViewModel.kt                   updateChildren, live listeneri
├── navigation/NavGraph.kt                      detalji čitaju iz liste uživo
└── ui/feature/
    ├── addcar/AddCarScreen.kt                  "Plin", phone, validacija
    └── detail/
        ├── DetailContact.kt                    NOV — prikaz i pozivanje broja
        └── DetailScreen.kt                     ubačen DetailContact
```
