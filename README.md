# 🧪 Автотесты для OTUS.ru с использованием Selenoid и Docker

Этот проект содержит набор автоматизированных тестов для сайта [otus.ru](https://otus.ru), реализованных с использованием **Java 17**, **Selenium WebDriver 4+**, **JUnit 5**, **Google Guice (DI)** и **Gradle**.  
Проект поддерживает запуск как локально, так и через **Selenoid** с возможностью эмуляции мобильных устройств.

---
##  :computer: Используемый стек

<p align="center">
<a href="https://www.jetbrains.com/idea/"><img width="10%" title="IntelliJ IDEA" src="media/logo/Intelij_IDEA.svg"></a>
<a href="https://www.java.com/"><img width="10%" title="Java" src="media/logo/Java.svg"></a>
<a href="https://www.selenium.dev/"><img width="10%" title="Selenium" src="media/logo/Selenium.svg"></a>
<a href="https://gradle.org/"><img width="10%" title="Gradle" src="media/logo/Gradle.svg"></a>
<a href="https://junit.org/junit5/"><img width="10%" title="JUnit5" src="media/logo/JUnit5.svg"></a>
</p>

---

## 📦 Структура проекта

```
src/
├─ main/java/
│  ├─ components/    # Web-компоненты (CourseListComponent, CourseCardComponent и др.)
│  ├─ pages/         # Page Object классы (BasePage, MainPage, CourseCatalogPage, CoursePage)
│  ├─ driver/        # Фабрика WebDriver (WebDriverProvider, BrowserFactory, BrowserType, SelenoidConfig)
│  ├─ di/            # Guice-модуль и расширение (TestModule, GuiceExtension)
│  └─ utils/         # Утилиты (HighlightingListener для подсветки выделяемых элементов)
└─ test/java/
   └─ UITests/       # Тестовые сценарии (CourseSearchTest, CourseDateTest, NavigationMenuTest)
```

---

## 🚀 Быстрый старт

1. **Клонировать репозиторий**
   ```bash
   git clone https://github.com/Aleksandr-A163/Selenoid-Citrus
   cd Selenoid-Citrus
   ```

2. **Запустить тесты (локально)**
   ```bash
   ./gradlew clean test
   ```

3. **Открыть отчёт**
   ```
   build/reports/tests/test/index.html
   ```

---


## ✅ Реализованные фичи

- **Dependency Injection** через Google Guice: все компоненты и страницы создаются через `@Inject`
- **JUnit 5 Extension** (`GuiceExtension`) вместо `BaseTest`
- **Подсветка элементов** перед действиями (`HighlightingListener`)
- **Декоратор WebDriver**: `EventFiringDecorator` для логирования и расширения поведения
- **Checkstyle + SpotBugs** подключены через Gradle
- **Page Object + Component Based Design**
- **Поддержка запуска через Selenoid (Docker)**

## Сценарии тестирования:

  - CourseSearchTest - Навигация на случайный курс из фиксированного списка
  - CourseDateTest - Поиск курса с самой ранней/поздней датой начала (с использованием Stream API и reduce)
  - NavigationMenutest - Переход на случайную категорию из меню «Обучение» и проверка URL


# 🚀 Запуск тестов CitrusProject через Selenoid

## 📦 Установка и запуск Selenoid

1. Перейдите в корень проекта, где находится `docker-compose.yml`.
2. Выполните:
   ```bash
   docker-compose up -d
   ```
3. После запуска:
   - **Панель управления:** [http://localhost:8080](http://localhost:8080)
   - **Эндпоинт Selenium:** `http://localhost:4444/wd/hub`

4. Проверка доступности браузеров:

   **PowerShell (Windows):**
   ```powershell
   curl http://localhost:4444/status | ConvertFrom-Json | Select-Object -ExpandProperty browsers
   ```

   **Linux / macOS:**
   ```bash
   curl http://localhost:4444/status | jq '.browsers'
   ```

   В ответ должно отображаться:
   ```json
   "browsers": {
     "chrome": { "123.0": { "image": "selenoid/chrome:123.0" } },
     "firefox": { "122.0": { "image": "selenoid/firefox:122.0" } },
     "edge": { "121.0": { "image": "selenoid/edge:121.0" } }
   }
   ```

---

## 🧪 Запуск тестов

### 🖥️ Обычный Chrome
```bash
./gradlew clean test -DrunMode=selenoid -Dbrowser=chrome
```

### 📱 Мобильный Chrome (эмуляция iPhone X)
```bash
./gradlew clean test -DrunMode=selenoid -Dbrowser=chromeMobile
```

---

## ⚙️ Параллельный запуск тестов

В `build.gradle` добавлен параметр:

```groovy
test {
    useJUnitPlatform()
    maxParallelForks = Runtime.runtime.availableProcessors().intdiv(2) ?: 1
}
```

Это позволяет запускать тесты параллельно (примерно на половине доступных CPU ядер).

---

## 🧰 Инфраструктура Selenoid

| Файл | Назначение |
|------|-------------|
| `docker-compose.yml` | Поднимает контейнеры `selenoid` и `selenoid-ui` |
| `config/browsers.json` | Список поддерживаемых браузеров и их версий |
| `src/test/java/driver/SelenoidConfig.java` | Конфигурирует RemoteWebDriver и эмуляцию |
| `src/test/resources/selenium/selenoid.properties` | Свойства браузера, версии и URL |
| `video/`, `logs/` | Каталоги для видео- и лог-файлов Selenoid |

---

## 🔧 Полезные команды Docker

```bash
docker ps                   # Список запущенных контейнеров
docker logs selenoid        # Логи контейнера Selenoid
docker logs selenoid-ui     # Логи интерфейса Selenoid UI
docker network ls           # Проверка наличия сети selenoid-net
docker network create selenoid-net  # Создание сети вручную (если отсутствует)
```

---

## 🧩 Файлы конфигурации

**Файл:** `src/test/resources/selenium/selenoid.properties`
```properties
selenoid.url=http://localhost:4444/wd/hub
browser=chrome
version=123.0
enableVNC=true
enableVideo=false
mobileEmulation.deviceName=iPhone X
```

**Файл:** `config/browsers.json`
```json
{
  "chrome": {
    "default": "123.0",
    "versions": {
      "123.0": {
        "image": "selenoid/chrome:123.0",
        "port": "4444"
      }
    }
  },
  "firefox": {
    "default": "122.0",
    "versions": {
      "122.0": {
        "image": "selenoid/firefox:122.0",
        "port": "4444"
      }
    }
  },
  "edge": {
    "default": "121.0",
    "versions": {
      "121.0": {
        "image": "selenoid/edge:121.0",
        "port": "4444"
      }
    }
  }
}
```

---

## 📧 Обратная связь

**Автор:** Aleksandr Anosov  
**GitHub:** [Aleksandr-A163](https://github.com/Aleksandr-A163)
