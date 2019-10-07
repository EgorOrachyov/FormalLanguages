# Formal Languages

University workshop for course 'Automata and formal languages theory'

# Instruction (Russian Version)

## Структура проекта

В корневом каталоге проетка лежат следующие файлы:

* PrimeNumsTM.txt - описание МТ, которая допускает язык простых чисел в унарной системе 
* PrimeNumsLBA.txt - описание LBA, который допускает язык простых чисел в унарной системе 
* PrimeNumsT0.txt - грамматика типа 0, построенная по МТ: выводятся только простые числа в унарной системе
* PrimeNumsT1.txt - грамматика типа 1, построенная по LBA: выводятся только простые числа в унарной системе

В папке src - java исходный код, необходимый для загрузки/преобразования грамматик и автоматов.

## Сборка и запуск

Чтобы собрать проект понадобиться Java SE SDK 12 версии, который можно скачать 
с официального сайта [Oracle](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html).

Проверить версию своего java компилятора можно проверить с помощью следующих команд:
```
$ javac -version
$ java -version
```

Здесь и далее редполагается, что вы открыли терминал по адресу корневой папки.
Чтобы собрать проект, используйте следующую команду:
```
$ javac -d build -so-urcepath src src/Application.java  
```

Если вы работаете в среде Linux/MacOS, для удобства использования вы можете
создать временный псевдоним:
```
$ alias automata="java -cp build Application"
```
Теперь в текущей сессии все команды, изложенные ниже, вы можете использавть
через alias automata.

## Запуск приложения

Все опции можно посмотреть, используюя следующую команду:
```
$ java -cp build Application -help
```
1. Сгенерировать грамматику типа 0 по машине Тьюринга:
```
$ java -cp build Application -t0 PrimeNumsMT.txt PrimeNumsT0.txt
```
2. Сгенерировать грамматику типа 1 по LBA:
```
$ java -cp build Application -t0 PrimeNumsLBA.txt PrimeNumsT1.txt
```
3. Вывести первые N простых чисел в грамматике типа 0:
```
$ java -cp build Application -d0 PrimeNumsT0.txt N
```
4. Вывести первые N простых чисел в грамматике типа 1:
```
$ java -cp build Application -d1 PrimeNumsT1.txt N
``` 
5. Проверить простоту числа N в грамматике типа 0:
```
$ java -cp build Application -p0 PrimeNumsT0.txt N
```
6. Проверить простоту числа N в грамматике типа 1:
```
$ java -cp build Application -p1 PrimeNumsT1.txt N
```

## Запуск приложения (Альтернатива с псевдонимом automata)

Все опции можно посмотреть, используюя следующую команду:
```
$ automata -help
```
1. Сгенерировать грамматику типа 0 по машине Тьюринга:
```
$ automata -t0 PrimeNumsMT.txt PrimeNumsT0.txt
```
2. Сгенерировать грамматику типа 1 по LBA:
```
$ automata -t0 PrimeNumsLBA.txt PrimeNumsT1.txt
```
3. Вывести первые N простых чисел в грамматике типа 0:
```
$ automata -d0 PrimeNumsT0.txt N
```
4. Вывести первые N простых чисел в грамматике типа 1:
```
$ automata -d1 PrimeNumsT1.txt N
``` 
5. Проверить простоту числа N в грамматике типа 0:
```
$ automata -p0 PrimeNumsT0.txt N
```
6. Проверить простоту числа N в грамматике типа 1:
```
$ automata -p1 PrimeNumsT1.txt N
```