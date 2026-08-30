# CS330 — Praktični zadatak (project250)

Android aplikacija za oglašavanje polovnih automobila.
Nikola Živković, 6090.

**Kotlin · Jetpack Compose · Firebase Realtime Database · Firebase Authentication**

## Šta aplikacija radi

| Ekran | Sadržaj |
|---|---|
| Home | Lista oglasa, pretraga po nazivu, filter po marki |
| Detalji | Specifikacije vozila, kontakt prodavca, akcije vlasnika |
| Dodaj / Izmeni oglas | Validirana forma za unos i izmenu |
| Moji oglasi | Sopstveni oglasi: pregled, izmena, brisanje |
| Omiljeni | Sačuvani oglasi |
| Profil | Podaci korisnika, izmena profila |

Podaci se čitaju preko trajnih pretplata na Realtime Database, pa se izmena
napravljena bilo gde odmah vidi u aplikaciji.

## Struktura

```
project250/app/src/main/java/com/zivkovic/project250/
├── domain/        modeli (CarModel, CategoryModel, UserProfile)
├── viewModel/     CarViewModel, CategoryViewModel
├── navigation/    NavGraph
└── ui/            komponente i ekrani po funkcionalnostima
```

## Pokretanje

Potreban je Android SDK i `google-services.json` u `project250/app/`.

```
cd project250
gradlew assembleDebug
```

## Sinhronizacija sa web aplikacijom (IT354)

Ova aplikacija deli Firebase projekat sa zasebnom web aplikacijom. Šema čvora
`Cars` je zajednički ugovor obe strane — nazivi i tipovi polja se ne menjaju
jednostrano.

- [CS330-PZ-Nikola-Dokumentacija-Zivkovic-6090.md](CS330-PZ-Nikola-Dokumentacija-Zivkovic-6090.md) — dokumentacija projekta
- [CS330-Poruka-Web-Agentu.md](CS330-Poruka-Web-Agentu.md) — pregled usklađivanja sa web stranom, sa zahtevima prema njoj
- [CS330-Odgovor-Android-Sinhronizacija.md](CS330-Odgovor-Android-Sinhronizacija.md) — detaljan odgovor na tačke ugovora o sinhronizaciji

> Sigurnosna pravila baze još nisu primenjena. Dok se to ne uradi, baza je
> otvorena za čitanje i upis — repozitorijum je zato privatan.
