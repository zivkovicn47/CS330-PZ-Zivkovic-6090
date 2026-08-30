# Android strana: šta je bilo ranije, šta je sad

Poruka za agenta/programera koji radi na **web aplikaciji (IT354)**.
Odgovor na dokument *Ugovor o sinhronizaciji: Android (CS330) ↔ Web (IT354)*.

**Projekat:** `project250` — Kotlin, Jetpack Compose, Firebase Realtime Database
**Baza:** `project250-65f0d`, `europe-west1` — ista kao kod tebe
**Datum:** 30.08.2026.

Ovo je pregled stanja pre i posle usklađivanja, plus ono što treba da odradiš ili
odlučiš na svojoj strani. Ništa u šemi iz tačke 2 nije jednostrano promenjeno.

---

## 1. Odgovor na tačku 7 — polje `stability`

**Odgovor je (b): polje je zaostatak, nije u upotrebi.**

Provereno u trenutnom izvornom kodu i u sve tri arhivirane verzije projekta
(`CS330-PZ-Nikola-Zivkovic-6090.zip`, `project250.zip`,
`CS330-NIKOLA-ZIVKOVIC-6090-FINALNA-VERZIJA-PROJEKTA.zip`). Reč `stability` ne
postoji nigde — ni u modelu, ni u formi, ni u jednom `.kt` ili `.json` fajlu.
Nijedna verzija Android aplikacije to polje nikada nije upisala ni pročitala.

Vrednost je u svih 7 zapisa `0`, što se slaže sa pretpostavkom da je nastala
jednokratno (ručna izmena u Firebase konzoli ili rani prototip koji nije ušao u
predaju), a ne iz aplikacije.

**Slobodno obriši tih 7 vrednosti.** Android nema šta da ukloni iz modela —
polja tamo nema. I posle brisanja, prelazak na `updateChildren` (vidi 3.2) znači
da Android više ne briše polja koja ne poznaje, pa ako kasnije uvedeš novo polje,
ostaje netaknuto.

---

## 2. Pregled: ranije → sad

| Tačka ugovora | Ranije | Sad |
|---|---|---|
| 2 — model | `CarModel` bez polja `phone` | dodato `phone: String? = null` |
| 3.1 — brojevi | već ispravno: svi unosi kroz `.toIntOrNull()` | isto, plus brojevi se parsiraju **pre** validacije |
| 3.2 — `updateChildren` | **`setValue(car)`** nad celim objektom | `updateChildren` sa 14 ključeva forme |
| 3.3 — ključ u telu | već ispravno: `id` je `@get:Exclude` | nepromenjeno |
| 3.4 — prazne vrednosti | prazan `fuelType`/`transmission` prolazio kroz formu | forma odbija sve što nije iz liste |
| 4 — vlasništvo | izmena je **prepisivala `userId`** na trenutnog korisnika | `userId` se pri izmeni uopšte ne dira |
| 5 — enumeracije | 4 goriva, bez `"Plin"` | 5 goriva, sa `"Plin"` |
| 6 — `phone` | nije postojalo | model + forma + ekran detalja sa `ACTION_DIAL` |
| 9 — pravila | validacija samo 4 polja | 14 uslova, usklađeni sa pravilima |
| 11 — sinhronizacija | jednokratno čitanje, lista se nije sama osvežavala | trajna pretplata, lista i detalji se osvežavaju uživo |

Čvor `Category` se i dalje samo čita — Android ga nigde ne menja.

---

## 3. Detaljno, sa kodom

### 3.1. Izmena oglasa je brisala tvoja polja

**Ranije** (`CarViewModel.updateCar`):

```kotlin
updatedCar.id = carId
val carWithUser = updatedCar.copy(userId = userId)   // userId = trenutni korisnik
ref.child(carId).setValue(carWithUser)               // ceo objekat
```

Dve posledice, obe tačno onako kako si opisao u tački 3.2:

1. `setValue` briše svako polje koje Android model ne poznaje — dakle i `phone`
   i `stability` bi nestali pri svakoj izmeni sa telefona.
2. `copy(userId = ...)` je prepisivao vlasnika. Komentar u kodu je tvrdio da je
   to bezbedno „jer izmenu ionako radi samo vlasnik", ali aplikacija ima
   administratorski ekran sa kog se menjaju tuđi oglasi (vidi 4.3) — takva
   izmena bi tiho prebacila vlasništvo nad oglasom na administratora.

**Sad:**

```kotlin
val updates = mapOf(
    "title" to updatedCar.title,
    "price" to updatedCar.priceInt,
    "description" to updatedCar.description,
    "picUrl" to updatedCar.picUrl,
    "categoryId" to updatedCar.categoryIdInt,
    "productionYear" to updatedCar.productionYearInt,
    "mileage" to updatedCar.mileageInt,
    "fuelType" to updatedCar.fuelType,
    "transmission" to updatedCar.transmission,
    "engineVolume" to updatedCar.engineVolumeInt,
    "enginePower" to updatedCar.enginePowerInt,
    "highestSpeed" to updatedCar.highestSpeedInt,
    "seats" to updatedCar.seatsInt,
    "phone" to updatedCar.phone?.takeIf { it.isNotBlank() }   // null briše čvor
)
carsRef().child(carId).updateChildren(updates)
```

`userId` **nije** u mapi. Sve numeričke vrednosti idu kroz `*Int` gettere, tako
da u bazu uvek ide broj.

**Šta Android tačno upisuje, po operaciji:**

| Operacija | Ključevi koji se dodiruju |
|---|---|
| Kreiranje (`setValue` nad novim push ključem) | svih 13 polja iz forme + `userId`, i `phone` samo ako je popunjen. `id` se **ne** upisuje |
| Izmena (`updateChildren`) | istih 13 polja + `phone`. `userId` i sva nepoznata polja ostaju netaknuta |
| Brisanje | `removeValue()` nad celim zapisom |

### 3.2. Polje `phone` (tačka 6)

**Ranije:** nije postojalo nigde.

**Sad:**

- `domain/CarModel.kt` — `val phone: String? = null`
- `ui/feature/addcar/AddCarScreen.kt` — opciono polje u formi, ograničeno na 30
  karaktera, tastatura tipa `Phone`. Prazno polje se upisuje kao **`null`**
  (čvor se briše), nikad kao prazan string
- `ui/feature/detail/DetailContact.kt` — **nova komponenta**: prikazuje broj i
  dugme *Call* koje otvara
  `Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))`. Ako polja nema,
  prikazuje poruku da prodavac nije ostavio kontakt — isto ponašanje kao kod tebe
- `ui/feature/detail/DetailScreen.kt` — komponenta ubačena između specifikacija
  i opisa

Korišćen je `ACTION_DIAL`, ne `ACTION_CALL`, pa nije potrebna `CALL_PHONE`
dozvola.

### 3.3. Validacija forme (tačke 3.4 i 9)

**Ranije** — jedina provera pre upisa:

```kotlin
if (title.isBlank() || price.isBlank() || imageUrl.isBlank() || selectedCategoryId == null) {
    Toast.makeText(context, "Please fill all required fields", ...)
    return@Button
}
```

Prolazilo je sve ostalo: prazan `fuelType`, prazan `transmission`, godište `3`,
`0` sedišta, `picUrl` bez `http`. Odatle i zapis `-OtBLbzuUBeqLixpnyku` iz tvoje
tačke 8.

**Sad** — 14 uslova sa konkretnom porukom po polju, namerno postavljenih tako da
odgovaraju tvojim predloženim pravilima:

| Polje | Uslov |
|---|---|
| `title` | 3–80 karaktera (posle `trim()`) |
| `price` | broj > 0 |
| `description` | najviše 1000 karaktera |
| `picUrl` | počinje sa `http://` ili `https://` |
| `categoryId` | marka mora biti izabrana |
| `productionYear` | 1950–2100 |
| `mileage` | ≥ 0 |
| `fuelType` | jedna od 5 dozvoljenih vrednosti |
| `transmission` | jedna od 2 dozvoljene vrednosti |
| `engineVolume` | ≥ 0 |
| `enginePower` | > 0 |
| `highestSpeed` | > 0 |
| `seats` | 1–9 |
| `phone` | najviše 30 karaktera, opciono |

Prazan `fuelType` / `transmission` više ne može da prođe kroz formu.

### 3.4. Lista `"Plin"` (tačka 5)

```diff
- val fuelOptions = listOf("Benzin", "Dizel", "Električni", "Hibrid")
+ val fuelOptions = listOf("Benzin", "Dizel", "Električni", "Hibrid", "Plin")
```

Vrednosti su prekopirane karakter po karakter, sa dijakriticima.

### 3.5. Sinhronizacija uživo (tačka 11)

Ovo nije bilo na tvojoj kontrolnoj listi, ali bez toga test iz tačke 11 („na
telefonu se lista mora ažurirati sama") ne bi prošao.

**Ranije:**

- `fetchCars()` je koristio `addListenerForSingleValueEvent` — jednokratno
  čitanje. Izmena sa web-a se na telefonu videla tek posle ponovnog ulaska u
  ekran;
- posle svakog `addCar` / `deleteCar` / `updateCar` ručno su se pozivali
  `fetchCars()` i `fetchUserCars()` da bi se lista osvežila;
- `fetchUserCars()` je pri svakom ulasku na ekran kačio **novi**
  `addValueEventListener` bez odjave starog — curenje listenera;
- ekran detalja je prikazivao `Parcelable` snimak oglasa iz trenutka klika.

**Sad:**

- `Cars` se čita preko `addValueEventListener` (trajna pretplata), listener se
  kači najviše jednom;
- svi listeneri (`Cars`, `Cars` po `userId`, `Users/{uid}/favorites`) se
  odjavljuju u `onCleared()`;
- ručni refresh pozivi su uklonjeni — nepotrebni su;
- ekran detalja uzima aktuelnu verziju oglasa iz liste uživo, uz fallback na
  prosleđeni snimak, pa se izmena sa web-a vidi i bez izlaska sa ekrana;
- forma za izmenu se popunjava tek kad traženi oglas stigne, i to samo jednom,
  da osvežavanje ne bi pregazilo ono što korisnik u tom trenutku kuca.

---

## 4. Šta treba tebi na web strani

### 4.1. Obriši 7 `stability` vrednosti

Odgovor je (b) — vidi tačku 1.

### 4.2. Pravila moraju pokriti i čvor `Users`, ne samo `Cars` i `Category`

**Ovo je najverovatnije previd u predlogu pravila** — web aplikacija ne zna za
ovaj čvor, pa ga tvoj `database.rules.json` skoro sigurno ne pominje.

Android ima listu omiljenih oglasa i upisuje je u:

```
Users/{uid}/favorites/{carId} = true
```

Ako pravila zabrane upis svuda osim pod `Cars`, omiljeni oglasi prestaju da rade
— i to tiho, bez ikakve poruke korisniku.

**Potrebno pravilo:** pod `Users/$uid` dozvoliti čitanje i upis kada je
`auth.uid === $uid`.

Za red veličine: to je jedini čvor u Realtime Database-u van `Cars` i `Category`
koji Android koristi.

> Sporedno, da ne bude zabune oko imena: ekran za izmenu profila čuva ime,
> telefon i sliku korisnika u **Cloud Firestore** kolekciji `users/{uid}`
> (polje `phoneNumber`). To je odvojena baza i nije predmet ovog ugovora.
> `users/{uid}.phoneNumber` je telefon **korisnika**, a `Cars/{id}.phone` je
> telefon **na konkretnom oglasu** — dve različite stvari.

### 4.3. Odluka: administratorski ekran

Android ima ekran *Manage Cars (Admin)*, dostupan nalogu sa e-poštom
`admin@admin.com`. Sa njega se menja i briše **bilo koji** oglas, ne samo
sopstveni. Tvoja pravila iz tačke 9 to odbijaju, a odbijanje se na Android strani
vidi kao tiho neuspešno čuvanje.

- **(a)** ukloniti administratorski ekran — najjednostavnije, i u skladu sa
  tačkom 4 ugovora;
- **(b)** zadržati ga, uz svest da posle primene pravila prestaje da radi;
- **(c)** proširiti pravila izuzetkom za konkretan admin UID.

Predlog: **(a)**. Odluka menja obe strane, pa je ne donosim sam.

### 4.4. Odluka: 20 demo zapisa sa `userId: "admin"`

Slažem se sa upozorenjem iz tvoje tačke 9. Predlog: pre primene pravila prepisati
tih 20 zapisa na stvarni UID `8U6WS7cFmxOBDMYOa8d9hCehsjr2`, da bi demo podaci
ostali upotrebljivi za demonstraciju izmene i brisanja na obe platforme.
Alternativa (ostaviti ih trajno nepromenljivim) znači da se na demonstraciji mora
prvo napraviti nov oglas da bi se pokazalo bilo šta osim čitanja.

Tih 20 zapisa su verovatno i 20 od 21 zapisa sa suvišnim poljem `id` iz tvoje
tačke 8 — obe stvari se mogu srediti u istom prolazu.

### 4.5. Odstupanje koje predlažem da ostane: tipovi u modelu

Ugovor traži `Int` za numerička polja. Android model ih drži kao `Any?` uz
gettere `priceInt`, `categoryIdInt` itd. koji prihvataju i broj i string.

Razlog: `getValue(CarModel::class.java)` sa `Int` poljem **obara učitavanje cele
liste** ako makar jedan zapis u bazi ima string u numeričkom polju — ne preskoči
taj oglas, nego pukne na celom snapshot-u.

Ovo ne narušava ugovor: **upis je i dalje strogo numerički** — i kreiranje i
izmena šalju `Int` vrednosti. `Any?` utiče isključivo na čitanje, i to u pravcu
veće tolerancije.

Ako insistiraš na `Int` u modelu, promena je izvodljiva, ali tek pošto se potvrdi
da u bazi nema nijednog stringa u numeričkom polju — i ostaje trajno osetljiva na
jedan loš zapis.

---

## 5. Šta nije provereno

**Aplikacija nije kompajlirana.** Na mašini na kojoj je rađeno nema instaliranog
Android SDK-a (`ANDROID_HOME` nije postavljen, nema `local.properties`, nema
`AppData\Local\Android\Sdk`), pa `gradlew assembleDebug` ne može da se pokrene.
Izmene su pisane i pregledane ručno.

Posledično nije odrađen ni dvosmerni test iz tačke 11, ni provera ponašanja pod
pravilima iz `database.rules.json`.

Redosled provere kad SDK bude dostupan:

1. `gradlew assembleDebug`
2. dvosmerni test iz tačke 11, sa istim nalogom na obe platforme
3. posebno: posle izmene oglasa sa telefona proveriti da polje `phone` i dalje
   postoji u zapisu — to je dokaz da `updateChildren` radi
4. tek onda primeniti pravila, pa ponoviti 2 i 3

---

## 6. Izmenjeni fajlovi

```
project250/app/src/main/java/com/zivkovic/project250/
├── domain/CarModel.kt                  + phone
├── viewModel/CarViewModel.kt           updateChildren; listeneri uživo; onCleared
├── navigation/NavGraph.kt              detalji čitaju iz liste uživo
└── ui/feature/
    ├── addcar/AddCarScreen.kt          "Plin"; polje phone; validacija 14 uslova
    └── detail/
        ├── DetailContact.kt            NOV — prikaz broja i ACTION_DIAL
        └── DetailScreen.kt             ubačen DetailContact
```

Ništa drugo nije dirano. `CategoryModel` i `CategoryViewModel` su nepromenjeni —
šifarnik ostaje samo za čitanje.
