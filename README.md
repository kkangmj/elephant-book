## 개발 환경

- Java 11
- Windows 10
- IntelliJ Ultimate

<br>

## Chapter 6. 압축 프로그래밍

> 프로그램 실행 시 eid_tags.zip 파일을 풀어 eid_tags.txt 파일을 resource 폴더 아래에 위치시켜야 한다.

### 요구사항

- eid_tags.txt 파일을 Gap과 VB Code로 부호화해 바이너리로 저장하는 프로그램을 만든다.
- 저장한 바이너리를 복원하는 프로그램을 만든다.

<br>

### Gap & VB Code 예시

- Gap
  ```text
  [3, 5, 20, 21, 23, 76, 77, 78] 
  => [3, 2, 15, 1, 2, 53, 1, 1]
  ```

- VB Code

  |  10진수   |            2진수             |           VB Code           | 
    |:-------:|:--------------------------:|:---------------------------:|
  |    5    |          00000101          |           1000101           |    
  |   128   |     00000000 10000000      |      00000001 10000000      |   
  |   700   |     00000010 10111100      |      00000101 10111100      | 
  | 1000000 | 00001111 01000010 01000000 | 00111101 10000100 110000000 |  

<br>

### 인코딩

- Topic의 길이(1 바이트), Tag의 개수(1 바이트), Topic을 eid_tags_encoded.bin 파일에 쓰기
- Tag의 Gap을 구해 배열에 저장
- 해당 배열을 파라미터로 Core 클래스의 encode() 호출
- encode()에서 리턴한 배열을 output 파일에 쓰기
- 각 라인마다 위 단계를 반복한다.

#### Core 클래스의 encode() 함수
```text
>> Input: [700]
> Initial value: 00000000 00000000 00000000 00000000
> 700 mod 128 = 60   -> 00000000 00000000 00000000 10111100
> 700 / 128 = 5.xx   -> 00000000 00000000 00000101 10111100
>> Output: [0, 0, 5, 188]
```

#### Output 파일
```text
hpmini110	17064731
-> Topic 길이: 9, Tag 길이: 1
```

eid_tags_encoded.bin 파일을 Hex Viewer로 열어보면 아래와 같다. 빨간색 박스는 Topic의 길이와 Tag의 길이, 초록색 박스는 Topic, 보라색 박스는 Tag 값이다.

![image](https://github.com/kkangmj/elephant-book/assets/52561963/42e392a5-d15c-4572-b5f0-40ef4e468979)

<br>

### 실행 결과

Gap과 VB Code로 정수열 데이터를 압축한 결과, 파일 크기가 약 **75%** 줄어든 것을 확인할 수 있다. 평균 소요 시간은 5회 실행의 평균으로 잡았다. (10132ms, 6693ms, 6791ms, 8125ms, 7945ms)

|         파일명          |     압축 방식     |    크기    | 평균 소요시간 | 
|:--------------------:|:-------------:|:--------:|:-------:|
|     eid_tags.txt     |       -       | 175660KB |    -    |
| eid_tags_encoded.bin | Gap + VB Code | 38877KB  | 7937ms  |


<br>

### 비하인드

#### 인코딩 시 BufferedWriter를 사용하지 않은 이유
- 처음에는 BufferedWriter 클래스의 bw.write()로 쓰기 작업을 진행했지만 정수를 쓸 때 데이터가 왜곡되는 현상을 발견했다.
- 예를 들어, VB Code 구현부인 Core 클래스의 encode() 메서드에 배열 [17064731]을 인자로 넘겨주면 배열 [8, 17, 70, 155]을 결과로 얻을 수 있다.
- BufferedWriter의 write() 메서드에 리턴 배열의 정수값을 넘기면 내부적으로 정수를 문자로 형변환해 버퍼에 쓰기 버퍼에 저장한다.
  ```java
  // BufferedWriter 클래스
  public void write(int c) throws IOException {
    synchronized (lock) {
        ensureOpen();
        if (nextChar >= nChars)
            flushBuffer();
        cb[nextChar++] = (char) c;
    }
  }
  ```
- 정수 155가 인자로 넘어가는 경우, 155는 로 변환되고 Hex 값은 C2 9B로 저장된다. 즉, 155는 1 바이트로 표현될 수 있음에도 char 변환에 의해 2 바이트로 저장된다는 문제가 발생한다. 
- 따라서, 불필요한 변환 과정을 제거하고 정수를 1 바이트로 저장하기 위해 DataOutputStream을 사용했다. 

#### 유용한 보조 사이트
- https://hexed.it/
- http://www.endmemo.com/unicode/unicodeconverter.php