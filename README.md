# SuperBank - გულის გასახარებელი ბანკი
##### SuperBank არის მობაილ ბანკი, რომელშიც დაიმპლემენტირებულია საბანკო სერვისები.
##### SuperBank_ში შეუძლია მომხმარებელს შეამოწმოს საკუთრ ბარათებზე ბალანსი, გადარიცხოს თანხა და სხვა.
##### SuperBank_ში რეგისტრაცია შეუძლაი ნებისმიერ მომხმარებელს Email_ის გამოყენებით.
![image](https://user-images.githubusercontent.com/107510454/187090364-037afbc2-04b3-47c5-bec2-b882db9aa9cd.png)


####  SuperBank_ის გამოყენება:
##### რეგისტრაციის სემდეგ იუზერი უკვე შეძლებს აპლიაკციით სარგებლობას, აპლიკაციაში იუზერს დახვდება:
- მიმდინარე ბალანსი
![image](https://user-images.githubusercontent.com/107510454/187090064-b21f981c-530a-469c-a012-6d8c10be59a6.png)

- ბარათები
![image](https://user-images.githubusercontent.com/107510454/187090079-60cf94c3-673e-4257-b6e9-583c88b55c28.png)
![image](https://user-images.githubusercontent.com/107510454/187090090-ebef3562-1916-43c4-ad8d-8454b0dbac57.png)

- ტრანზაქციები
![image](https://user-images.githubusercontent.com/107510454/187090109-0a36627c-ae25-4b33-8f7b-bd475caafb9f.png | width=100)
![image](https://user-images.githubusercontent.com/107510454/187090473-135ebb70-af31-4360-a053-72444e86eb99.png)

- საბანკო შეთავაზებები
![image](https://user-images.githubusercontent.com/107510454/187090142-cd426384-a393-4314-ba5d-2872e25c5b03.png)

- ვალუტის კალკულატორი
![image](https://user-images.githubusercontent.com/107510454/187090152-7504c523-287c-48a6-a803-ca18c7f907af.png)

- შეძლებს გადარიცხვას მობილურის ან ანგარიშის ნომრით
![image](https://user-images.githubusercontent.com/107510454/187090167-f52c970f-d1ab-4efc-84e8-56bbef49c9d9.png)

- მეილის და პაროლის შეცვლას.
![image](https://user-images.githubusercontent.com/107510454/187090188-bef9d822-f4f5-41a0-9672-bf86cdcf295f.png)
![image](https://user-images.githubusercontent.com/107510454/187090196-8b055376-9ccf-4126-b2f4-fbcc4a59ceea.png)

####  SuperBank_ში გამოყენებულია:
- Firebase - რეგისტარცია ავტორიზააციისთვის, სესიი განსაზღვრა ხდება firebase_ის იუზერი გამოყენებით.
- API - სერვისები აწყობილია REST ტექნოლოგიაზე.
- სრედებთან სამუშაოდ გამოყენებულია კორუტინები.
- Networking - ხდება Retorifit Client_ის საშუალებით.
- აპლიკაცაი აწყობილია Single Activity პრინციპით.
- ნავიგაციას აგვარებს Jetpack Navigation
-Splash screen - ლოგოს გამოსაჩენად აპლიკაციის ჩატვირთვამდე
![image](https://user-images.githubusercontent.com/107510454/187090298-b5294628-f214-446e-a3cf-a4c3d75382d8.png)
![image](https://user-images.githubusercontent.com/107510454/187090376-af4a9a0d-8c68-4d28-920d-19f6d89c1dd7.png)

#### უსაფრთხოება - იუზერის სესია:
- აპლიკაციით ვერ შეძლებს იუზერი სარგებლობას თუ არა რის დალოგინებული.
- შენახული სესიის ყველა ფრაგმენტზე მოწმდება და შემდგომ იტვირთება გვერდი.
- Firebase_ის კონსოლიდან დადისეიბლებული ან წაშლილი იუზერები ვეღარ შეძლებენ სესიის დაწყებას ან გაგრძელებას.
-ინტერნეტთან კავშირის არ არსებობის შემთხვევაში მომხმარებელს არ ექნება წვდომა დალოგინებულ მომხმარებლისთვის განკუთვნილ ინტერფეისზე
![image](https://user-images.githubusercontent.com/107510454/187090456-8cae709d-9866-4a0d-9f82-23a313685aa1.png)
