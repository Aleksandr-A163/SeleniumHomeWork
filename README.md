# 🧪 Автотесты для OTUS.ru

Этот проект содержит набор автоматизированных тестов для сайта [otus.ru](https://otus.ru), реализованных с использованием **Java 17**, **Selenium WebDriver 4+**, **JUnit 5**, **Google Guice (DI)** и **Gradle**.

##  :computer: Используемый стек

<p align="center">
<a href="https://www.jetbrains.com/idea/"><img width="6%" title="IntelliJ IDEA" src="media/logo/Intelij_IDEA.svg"></a>
<a href="https://www.java.com/"><img width="6%" title="Java" src="media/logo/Java.svg"></a>
<a href="https://www.selenium.dev/"><img width="6%" title="Selenium" src="media/logo/Selenium.svg"></a>
<a href="https://gradle.org/"><img width="6%" title="Gradle" src="media/logo/Gradle.svg"></a>
<a href="https://junit.org/junit5/"><img width="6%" title="JUnit5" src="media/logo/JUnit5.svg"></a>
</p>

---

## 📦 Структура проекта

```
src/
├─ main/java/
│  ├─ components/    # Web‑компоненты (CourseListComponent, CourseCardComponent и др.)
│  ├─ pages/         # Page Object классы (BasePage, MainPage, CourseCatalogPage, CoursePage)
│  ├─ driver/        # Фабрика WebDriver (WebDriverProvider, BrowserFactory, BrowserType )
│  ├─ di/            # Guice‑модуль и расширение (TestModule, GuiceExtension)
│  └─ utils/         # Утилиты (HighlightingListener для подсветки выделяемых объектов)
└─ test/java/
   └─ scenarios/     # Тестовые сценарии (CourseSearchTest, CourseDateTest и NavigationMenutest)
```

---

## 🚀 Быстрый старт

1. **Клонировать репозиторий**

   ```bash
   git clone https://github.com/Aleksandr-A163/SeleniumHomeWork
   cd SeleniumHomeWork
   ```

2. **Запустить тесты**

   Запуск всех тестов по умолчанию (браузер Chrome):

   ```bash
   ./gradlew clean test
   ```

   Запуск в конкретном браузере:

   ```bash
   ./gradlew test -Dbrowser=chrome
   ./gradlew test -Dbrowser=firefox
   ./gradlew test -Dbrowser=edge
   ```

3. **Открытие отчёта**

   После завершения выполнения откройте:

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

## Сценарии тестирования:

  - CourseSearchTest - Навигация на случайный курс из фиксированного списка
  - CourseDateTest - Поиск курса с самой ранней/поздней датой начала (с использованием Stream API и reduce)
  - NavigationMenutest - Переход на случайную категорию из меню «Обучение» и проверка URL

---

## 📌 TODO / Планы развития

- [ ] Миграция тестов на Cucumber


---

## 📧 Обратная связь

Автор: Aleksandr Anosov  
GitHub: [Aleksandr-A163](https://github.com/Aleksandr-A163)
