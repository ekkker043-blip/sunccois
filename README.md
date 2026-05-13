# SunccoisEconomy

Кросс-серверная экономика для Minecraft-сети на **Paper + Velocity**
с хранением в **MySQL**, защитой от дюпа и форматированным выводом (`1,000`, `10,000`).

## Что внутри

- `paper-plugin/` — плагин для каждого backend-сервера Paper.
- `velocity-plugin/` — плагин для Velocity-прокси. Ретранслирует сообщения инвалидации кеша между всеми backend-серверами.
- `common/` — общие классы (форматтер чисел, константы канала сообщений).

## Команды

Все команды требуют право `sunccois.eco.admin`, кроме `/eco get`.

| Команда | Что делает |
|---|---|
| `/eco give <ник> <кол-во>` | Выдать игроку монет |
| `/eco take <ник> <кол-во>` | Забрать у игрока монет |
| `/eco reset <ник>` | Обнулить баланс |
| `/eco get [ник]` | Посмотреть баланс (свой или чужой, нужен `sunccois.eco.get`) |

Количество можно писать с запятыми или подчёркиваниями: `1,000`, `10_000`.

## PlaceholderAPI

- `%sunccois_balance%` — число как есть (`1000`)
- `%sunccois_balance_formatted%` — с запятыми (`1,000`)

## Антидюп: как это работает

1. Каждая мутация баланса — одна транзакция InnoDB:
   `SELECT ... FOR UPDATE` → `UPDATE` → `INSERT INTO eco_transactions` → `COMMIT`.
2. Блокировка строки исключает гонки, даже если команды прилетели с двух разных Paper-серверов одновременно.
3. Проверки `amount > 0`, `balance + delta >= 0`, overflow через `Math.addExact`, `balance <= maximum-balance`. Чек `CHECK (balance >= 0)` на уровне схемы как страховка.
4. **Никакого отложенного сохранения.** К моменту, когда команда ответила «успех», данные уже закоммичены в MySQL. `kill -9` или перезагрузка ничего не теряют.
5. Read-cache (Caffeine, 5с по умолчанию) — только для чтений/плейсхолдера. Любая запись инвалидирует его локально и рассылает `INVALIDATE` через Velocity на все серверы.

## Аудит

Каждая операция пишется в таблицу `eco_transactions`:

| поле | смысл |
|---|---|
| `account_uuid` | кому |
| `type` | `GIVE` / `TAKE` / `RESET` |
| `delta` | насколько изменился баланс (может быть отрицательным) |
| `balance_after` | остаток после операции |
| `source_server` | имя backend-сервера (`server-name` в config.yml) |
| `actor` | `ник игрока` или `CONSOLE` |
| `created_at` | время с точностью до миллисекунды |

Так вы всегда видите, **где** и **как** начислили/списали монеты.

## Установка

1. **MySQL.** Создайте БД и пользователя:
   ```sql
   CREATE DATABASE sunccois CHARACTER SET utf8mb4;
   CREATE USER 'sunccois'@'%' IDENTIFIED BY 'change_me';
   GRANT ALL ON sunccois.* TO 'sunccois'@'%';
   FLUSH PRIVILEGES;
   ```
2. **Velocity.** Положите `SunccoisEconomy-Velocity-<версия>.jar` в `plugins/` прокси. Перезапустите.
3. **Каждый Paper-сервер.** Положите `SunccoisEconomy-Paper-<версия>.jar` в `plugins/`, запустите один раз, остановите, отредактируйте `plugins/SunccoisEconomy/config.yml`:
   - `server-name` — **уникальное** имя этого сервера (`lobby`, `survival`, `creative`…)
   - `database.*` — доступ к MySQL
4. Убедитесь, что Velocity и все backend-серверы видят друг друга по плагин-каналу (это стандартная настройка Velocity, ничего руками делать не нужно — канал регистрируется автоматически).

## Как получить готовые `.jar`

Из-за сетевых ограничений сборочного окружения я не могу прикрепить `.jar` прямо в репозиторий, но в нём уже есть GitHub Actions workflow `.github/workflows/build.yml`, который:

- собирает оба плагина на каждом push / PR,
- публикует артефакт `SunccoisEconomy-jars` со всеми jar'ами в **Actions → Workflow run → Artifacts**,
- автоматически создаёт **Release** с прикреплёнными jar'ами при пуше тега `v*` (например `v1.0.0`).

### Локальная сборка (если хотите собрать сами)

Требуется JDK 21 и Maven 3.9+.

```bash
mvn clean package
# Готовые jar:
#   paper-plugin/target/SunccoisEconomy-Paper-1.0.0.jar
#   velocity-plugin/target/SunccoisEconomy-Velocity-1.0.0.jar
```

## Совместимость

- Paper API: 1.20+ (тестируется против 1.21.4).
- Velocity API: 3.3.0+.
- MySQL 8.x (работает и с 5.7, но 8+ предпочтительнее).
- Java 21.
