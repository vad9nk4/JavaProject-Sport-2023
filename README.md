# JavaProject-Sport-2023

### 1. Создадим два файла для хранения данных извлечённых из csv - PlayerIndicators и Team

Файлы PlayerIndicators и Team представляют собой классы, используемые в приложении для представления структуры данных. Они служат для моделирования сущностей, с которыми приложение работает, а именно, игроков и команд.
* Team - Представляет команду, включающую в себя список игроков и другие характеристики команды
* PlayerIndicators - Описывает показатели игрока, такие как имя, позиция, рост, вес и возраст.
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/30586468-526b-4baf-810d-d66957576329)
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/33cf1464-e6de-40eb-b246-3f9263e47911)
<br /><br /><br />

### 2. Создаём класс CSVHandler, который будет служить для обработки данных в формате CSV (Comma-Separated Values), который часто используется для представления табличных данных. В этом приложении, класс CSVHandler выполняет роль чтения данных из CSV файла и преобразования их в формат, который можно использовать в приложении для дальнейшей обработки.
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/f6ae4d09-47b2-4f7b-9124-4353960f6593)

<br /><br /><br />

### 3. Создаём класс SQLHandler, который будет выполнять роль взаимодействия с базой данных SQLite и предоставляет методы для выполнения различных операций с данными. Он используется для создания, чтения, обновления и удаления данных в базе данных, а также для выполнения запросов и получения статистических данных для анализа спортивных данных.
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/658e5f46-ed82-479d-b1c3-b689b3a1975c)

<br /><br /><br />

### 4. Читаем данные ( столбцы ), и создаём их в таблице базы данных SQLite, после чего передаём туда данных из csv-файла.
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/995b206f-7ea5-4348-b2d2-e5aac9b5daed)

<br /><br /><br />

### 5. Создаём метод getAllData в классе SQLHandler, который перебирает всю БД и возвращает все данные из неё и в последствии выводим эти данные на экран пользователя в консоли.
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/3a661183-0508-43ba-ab71-d1b89e3b4f77)
* Вывод данных в консоль:<br />
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/63503bb4-fbfe-43e7-8fcc-e61801da2dcd)

<br /><br /><br />

### 6. Создаём метод который выполняет первое задание, а именно строит график среднего возраста команд.

* Достаём данные из базы данных:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/246e220b-8cda-4bc1-a1f4-4ce3cef550f0)


* Создаём график: <br />
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/e019413f-552b-4ce7-9f1b-831621e155d9)
<br />
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/12b750a5-8c2e-412d-b3d9-b1c1413e5a11)
<br /><br /><br />


### 7. Полученный график:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/8df31994-74b4-415f-bf10-756c9ae85213)

<br /><br /><br />

### 8. Создаём метод для второго задания, а именно: "Найдите команду с самым высоким средним ростом. Выведите в консоль 5 самых высоких игроков команды."

* Делаем нужные запросы из БД:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/13761ed7-8de5-489e-82ee-97abe97b494a)

* Создаём метод для вывода в консоль:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/2f4beb84-40bf-4ac5-95ac-d81506b33a70)

<br /><br /><br />

### 9. Создаём метод для третьего задания, а именно "Найдите команду, с средним ростом равным от 74 до 78 inches и средним весом от 190 до 210 lbs, с самым высоким средним возрастом."
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/7ec5e5f2-7450-4030-9298-460713057df9)

<br /><br /><br />

### 10. Итоговый вывод результата выполнения программой заданий:
![image](https://github.com/vad9nk4/JavaProject-Sport-2023/assets/134198984/85c26fb5-5df6-47ec-ae4e-b3138c04afb0)

<br /><br /><br />
