# Order Service

Sipariş, sipariş kalemi ve sepet işlemlerini yöneten; ürün stok/uygunluk bilgisiyle senkron çalışan ve sipariş ile stok değişikliklerini Kafka üzerinden yayınlayan Spring Boot servisi.

---

## İçindekiler
- [Genel Bakış](#genel-bakış)
- [Mimari](#mimari)
- [Teknolojiler](#teknolojiler)
- [Veritabanı](#veritabanı)
- [API Endpoints](#api-endpoints)
- [Servisler Arası İletişim](#servisler-arası-i̇letişim)
- [Kurulum](#kurulum)
- [Ortam Değişkenleri](#ortam-değişkenleri)
- [Testler](#testler)

---

## Genel Bakış

Order Service, LendMate sistemi içindeki sipariş yaşam döngüsünün sahibidir.

- Sipariş, sipariş kalemi ve sepet kayıtlarını yönetir.
- Sipariş oluşturulurken ürün servisinden stok miktarlarını kontrol eder.
- Sipariş kalemleri için ürün uygunluk kaydı oluşturur ve işlem sonrasında kullanıcının sepetini temizler.
- Ödeme durumuna göre `PENDING` siparişleri `CONFIRMED`, `CONFIRMED` siparişleri `DELIVERED` durumuna taşır.
- İşlem başarılı şekilde commit edildikten sonra sipariş ve stok azaltma olaylarını Kafka'ya gönderir.
- OpenAPI arayüzü ve Actuator desteği içerir.

## Mimari

### Katmanlar

- **Controller:** HTTP isteklerini karşılar ve request/response DTO'larını kullanır.
- **Service:** Sipariş, sipariş kalemi ve sepet iş kurallarını yürütür.
- **Repository:** Spring Data JPA üzerinden PostgreSQL verilerine erişir.
- **Mapper:** Entity ve DTO dönüşümlerini gerçekleştirir.
- **Client:** OpenFeign ile Product Service'e senkron çağrılar yapar.
- **Event/Kafka:** Transaction commit sonrasında sipariş ve stok olaylarını yayınlar.
- **Config:** OpenFeign, Kafka, Swagger, REST istemcisi ve cron yapılandırmalarını içerir.

### Klasör Yapısı

```text
src/main/java/com/lendmate/orderservice/
├── client/       # Product Service istemcisi ve mock istemciler
├── config/        # Uygulama, Swagger, Kafka ve scheduling yapılandırmaları
├── controller/   # REST endpoint'leri
├── dto/          # Request ve response modelleri
├── event/        # Uygulama olayları ve listener'lar
├── exception/    # Exception sınıfları ve global handler
├── kafka/        # Kafka event modelleri, producer ve config
├── mapper/       # Entity/DTO mapper'ları
├── model/        # JPA entity'leri ve enum'lar
├── repository/   # JPA repository'leri
└── service/      # Servis arayüzleri ve implementasyonları
```

## Teknolojiler

| Teknoloji | Versiyon | Kullanım Amacı |
|---|---|---|
| Java | 21 | Uygulama çalışma zamanı |
| Spring Boot | 3.5.15 | Uygulama çatısı |
| Spring Web | Spring Boot ile | REST API |
| Spring Data JPA | Spring Boot ile | ORM ve veri erişimi |
| PostgreSQL | Harici servis | Üretim veritabanı |
| Flyway | Spring Boot ile | Veritabanı migration yönetimi |
| Spring Cloud OpenFeign | 2025.0.0 BOM | Product Service çağrıları |
| Apache Kafka | Spring Kafka | Asenkron olay iletişimi |
| SpringDoc OpenAPI | 2.8.16 | API dokümantasyonu |
| Spring Cloud Config | 2025.0.0 BOM | Merkezi yapılandırma |
| OpenTelemetry | 2.6.0 | Gözlemlenebilirlik |
| Actuator | Spring Boot ile | Sağlık ve operasyon endpoint'leri |

## Veritabanı

### Tablolar

- `orders`: Sipariş numarası, kullanıcı, adres, toplam tutar ve durum bilgileri.
- `order_item`: Siparişe bağlı ürün, miktar, birim fiyat ve kiralama tarihleri. `orders` tablosuna foreign key ile bağlıdır; sipariş silinince kalemleri de silinir.
- `cart`: Kullanıcıların sepetindeki ürünler.
- `cron_config`: Scheduling için cron ifadeleri ve iş adları.
- `order_seq`: Sipariş numarası üretiminde kullanılan sequence; başlangıç değeri `10000000000`'dır.

Flyway migration'ları `src/main/resources/db/migration` altında `V1`-`V6` arasında bulunur.

## API Endpoints

### Orders

| Method | Endpoint | Açıklama | Auth |
|---|---|---|---|
| GET | `/orders/health` | Servis sağlık kontrolü | Yok |
| POST | `/orders` | Yeni sipariş oluşturur | Yok |
| GET | `/orders/{id}` | ID ile sipariş getirir | Yok |
| PUT | `/orders/{id}` | Siparişi günceller | Yok |
| DELETE | `/orders/{id}` | Siparişi siler | Yok |
| GET | `/orders/user/{userId}` | Kullanıcının teslim edilmiş siparişlerini getirir | Yok |
| GET | `/orders/convert-pending-to-confirmed` | Başarılı ödeme durumundaki bekleyen siparişleri onaylar | Yok |
| GET | `/orders/convert-confirmed-to-delivered` | Başarılı teslimat durumundaki onaylı siparişleri teslim edildi yapar | Yok |

`POST /orders` gövdesi `userId`, `status`, `totalPrice`, `addressId` ve isteğe bağlı `description` ile `items` alanlarını kabul eder. Sipariş kalemlerinde `productId`, `quantity`, `unitPrice`, `startDate` ve `endDate` zorunludur.

### Order Items

| Method | Endpoint | Açıklama | Auth |
|---|---|---|---|
| POST | `/order-items/orders/{orderId}` | Sipariş kalemi oluşturur | Yok |
| GET | `/order-items/orders/{orderId}` | Siparişin kalemlerini getirir | Yok |
| DELETE | `/order-items/{id}` | Sipariş kalemini siler | Yok |

### Carts

| Method | Endpoint | Açıklama | Auth |
|---|---|---|---|
| POST | `/carts` | Sepete ürün ekler | Yok |
| GET | `/carts/users/{userId}` | Kullanıcının sepetini ürün detaylarıyla getirir | Yok |
| DELETE | `/carts/{id}` | Sepet kaydını siler | Yok |

OpenAPI arayüzü varsayılan olarak `/swagger-ui.html`, OpenAPI JSON çıktısı `/v3/api-docs` adresindedir. Güvenlik starter'ı pom dosyasında yorum satırında olduğundan endpoint'lerde uygulama içi auth zorunluluğu bulunmamaktadır.

## Servisler Arası İletişim

### Feign Client (Senkron)

`ProductServiceClient`, `product-service` adıyla aşağıdaki çağrıları yapar:

- `GET /products/batch?ids=...`: Ürün detaylarını toplu getirir.
- `POST /product-availability`: Ürün uygunluk kaydı oluşturur.
- `POST /products/internal/quantities`: Ürün stok miktarlarını toplu getirir.

### Kafka Events (Asenkron)

Olaylar transaction commit sonrasında yayınlanır:

| Topic | Olay | Amaç |
|---|---|---|
| `order-topic` | `OrderEvent` | Sipariş olayını ilgili servislere iletmek |
| `quantity-decrease-topic` | `StockDecreaseEvent` | Sipariş ürünlerinin stok miktarını azaltmak |

Kafka producer sınıfları `OrderProducer` ve `StockProducer`; uygulama olay listener'ları ise `OrderEventListener` ve `StockEventListener` sınıflarıdır.

## Kurulum

### Gereksinimler

- JDK 21
- Maven Wrapper (`./mvnw`)
- PostgreSQL
- Çalışan Product Service
- Kafka (event akışları kullanılacaksa)
- Config Server (stage/prod profilleri için)

### Çalıştırma

Yerel geliştirme profilinde:

```bash
./mvnw spring-boot:run
```

Varsayılan servis adresi `http://localhost:8082`'dir. Docker ile çalıştırmak için `lendmate-net` harici Docker network'ünün ve bağımlı servislerin hazır olduğundan emin olun:

```bash
docker compose -f docker-compose-local.yml up --build
```

Production benzeri compose dosyası için:

```bash
docker compose up --build
```

## Ortam Değişkenleri

| Değişken | Açıklama | Örnek |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Aktif Spring profili | `dev` / `stage` / `prod` / `test` |
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC bağlantı adresi | `jdbc:postgresql://localhost:5432/order_service_db` |
| `SPRING_DATASOURCE_USERNAME` | Veritabanı kullanıcı adı | `lendmate` |
| `SPRING_DATASOURCE_PASSWORD` | Veritabanı parolası | `***` |
| `CONFIG_SERVER_URL` | Spring Config Server adresi | `http://localhost:8888` |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | OpenTelemetry collector adresi | `http://localhost:4318` |
| `OTEL_SERVICE_NAME` | Telemetri servis adı | `order-service` |

`SPRING_PROFILES_ACTIVE` verilmezse `dev` profili kullanılır. Dev profilinde Config Server import'u opsiyoneldir; stage/prod profillerinde varsayılan adres `http://config-server:8888`'dir. Veritabanı ve Kafka ayarları merkezi yapılandırmadan veya çalışma ortamından sağlanır.

## Testler

Testleri çalıştırmak için:

```bash
./mvnw test
```

Test profilinde scheduling devre dışıdır. Test bağımlılıkları arasında H2, Spring Boot Test ve Spring Security Test bulunur.