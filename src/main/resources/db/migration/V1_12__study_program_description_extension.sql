alter table study_program_details
    add column if not exists general_information  varchar(8000),
    add column if not exists graduation_title     varchar(3000),
    add column if not exists graduation_title_en     varchar(3000),
    add column if not exists subject_restrictions varchar(8000),
    add column if not exists on_english bool default false;

update study_program_details
set
    general_information='Додипломските студии по Примена на информациски технологии имаат за цел обезбедување на врвен информатички кадар за задоволување на се поголемата побарувачка за информатички инженери кои покрај техничките знаења имаат и бизнис знаења и претприемачки дух. Овие студии ќе создаваат кадар кој ќе има знаење за алатките и принципите на раководење со проекти со значајно учество на информатичката технологија од една страна, но и примена на современи информатички алатки во процесот на управување со проекти или претпријатија од друга страна. Дипломците од овие студии кои ќе бидат способни да дадат поддршка на системите што во својата работа на апликативно ниво користат иновативни технологии.',
    graduation_title='Дипломиран инженер по информатички науки и компјутерско инженерство, насока Примена на информациски технологии',
    graduation_title_en='Bachelor of Science in Software Engineering and Information Technologies (240 ECTS)'
where id='PIT23';


update study_program_details
set
    general_information='Додипломските студии по Примена на информациски технологии имаат за цел обезбедување на врвен информатички кадар за задоволување на се поголемата побарувачка за информатички инженери кои покрај техничките знаења имаат и бизнис знаења и претприемачки дух. Овие студии ќе создаваат кадар кој ќе има знаење за алатките и принципите на раководење со проекти со значајно учество на информатичката технологија од една страна, но и примена на современи информатички алатки во процесот на управување со проекти или претпријатија од друга страна. Дипломците од овие студии кои ќе бидат способни да дадат поддршка на системите што во својата работа на апликативно ниво користат иновативни технологии.',
    graduation_title='Дипломиран инженер по информатички технологии, насока Примена на информациски технологии',
    graduation_title_en='Bachelor of Science in Software Engineering and Information Technologies (180 ECTS)'
where id='PIT23_3';

update study_program_details
set
    general_information='Овие студии имаат за цел обезбедување на врвен инженерски кадар за задоволување на се поголемата побарувачка за софтверски инженери, особено програмери. Студентите на оваа студиска програма се здобиваат со знаења кои им обезбедуваат голема професионална флексибилност и широк избор на работни места секаде каде постои потреба од анализа, спецификација, дизајн, проектирање, имплементација и програмирање, проектно управување и одржување на софтвер и информациски системи. Дипломираниот инженер од овој профил својата кариера може да ја гради во софтверската индустријата, во јавниот и услужениот сектор, но, особено во компаниите за развој на софтвер, кај информациските системи што треба да работат во реално време, мобилните и хибридни апликации, софтвери кои работат на интернет и во облак, напредни кориснички интерфејси, решенија поврзани со дизајн и имплементација на софтверски системи и друго.',
    graduation_title='Дипломиран инженер по информатички науки и компјутерско инженерство, насока софтверско инженерство и информациски системи',
    graduation_title_en='Bachelor of Science in Software Engineering and Information Technologies (240 ECTS)'
where id='SIIS23';


update study_program_details
set
    general_information='Овие студии имаат за цел обезбедување на врвен инженерски кадар за задоволување на се поголемата побарувачка за софтверски инженери, особено програмери. Студентите на оваа студиска програма се здобиваат со знаења кои им обезбедуваат голема професионална флексибилност и широк избор на работни места секаде каде постои потреба од анализа, спецификација, дизајн, проектирање, имплементација и програмирање, проектно управување и одржување на софтвер и информациски системи. Дипломираниот инженер од овој профил својата кариера може да ја гради во софтверската индустријата, во јавниот и услужениот сектор, но, особено во компаниите за развој на софтвер, кај информациските системи што треба да работат во реално време, мобилните и хибридни апликации, софтвери кои работат на интернет и во облак, напредни кориснички интерфејси, решенија поврзани со дизајн и имплементација на софтверски системи и друго.',
    graduation_title='Дипломиран инженер по информатички технологии, насока софтверско инженерство и информациски системи',
    graduation_title_en='Bachelor of Science in Software Engineering and Information Technologies (180 ECTS)'
where id='SIIS23_3';


update study_program_details
set
    general_information='Undergraduate studies in the according study program should provide for superb engineering staff capable of satisfying the ever-increasing demand for software engineers, especially programmers. Students enrolled in this study program acquire knowledge enabling them great professional flexibility and wide choice of employment opportunities wherever there is a need of analysis, specification, design, planning, implementation and programming, project management and maintaining software and information systems. Bachelor of Engineering of this profile can build his own career in software industry, in public and service sector, but especially in software development companies, real-time information systems, mobile and hybrid applications, internet and cloud software, advanced user interfaces, solutions related to design and implementation of software systems, etc.',
    graduation_title='',
    graduation_title_en='Bachelor of Science in Software Engineering and Information Technologies (240 ECTS)'
where id='SEIS23';

update study_program_details
set
    general_information='Undergraduate studies in the according study program should provide for superb engineering staff capable of satisfying the ever-increasing demand for software engineers, especially programmers. Students enrolled in this study program acquire knowledge enabling them great professional flexibility and wide choice of employment opportunities wherever there is a need of analysis, specification, design, planning, implementation and programming, project management and maintaining software and information systems. Bachelor of Engineering of this profile can build his own career in software industry, in public and service sector, but especially in software development companies, real-time information systems, mobile and hybrid applications, internet and cloud software, advanced user interfaces, solutions related to design and implementation of software systems, etc.',
    graduation_title='',
    graduation_title_en='Bachelor of Science in Software Engineering and Information Technologies (180 ECTS)'
where id='SEIS23_3';



update study_program_details
set
    general_information='Додипломските студии по компјутерски науки имаат за цел обезбедување на врвен кадар за задоволување на сé поголемата побарувачка за инженери по информатички науки и компјутерско инженерство. Студентите на оваа студиска програма се здобиваат со знаења кои им обезбедуваат голема професионална флексибилност и широк избор на работни места секаде каде постои потреба од примена на вештини за решавање на проблеми, како и вештини за критичко размислување, кои ќе ги користат за решавање на комплексни пресметковни проблеми. Дипломираните кадри ќе можат лесно да најдат работа во компаниите и истражувачките центри. Освен што би биле обучени да учествуваат во развивање, имплементација и одржување на најразлични софтверски апликации, тие ќе ја имаат и потребната основа да продолжат со постдипломски студии, како и да учествуваат во интердисциплинарни проекти од науката и технологијата.',
    graduation_title='Дипломиран инженер по информатички науки и компјутерско инженерство, насока Компјутерски науки',
    graduation_title_en='Bachelor of Science in Computer Science (240 ECTS)'
where id='KN23';


update study_program_details
set
    general_information='Додипломските студии по компјутерски науки имаат за цел обезбедување на врвен кадар за задоволување на сé поголемата побарувачка за инженери по информатички науки и компјутерско инженерство. Студентите на оваа студиска програма се здобиваат со знаења кои им обезбедуваат голема професионална флексибилност и широк избор на работни места секаде каде постои потреба од примена на вештини за решавање на проблеми, како и вештини за критичко размислување, кои ќе ги користат за решавање на комплексни пресметковни проблеми. Дипломираните кадри ќе можат лесно да најдат работа во компаниите и истражувачките центри. Освен што би биле обучени да учествуваат во развивање, имплементација и одржување на најразлични софтверски апликации, тие ќе ја имаат и потребната основа да продолжат со постдипломски студии, како и да учествуваат во интердисциплинарни проекти од науката и технологијата.',
    graduation_title='Дипломиран инженер по информатички технологии, насока Компјутерски науки',
    graduation_title_en='Bachelor of Science in Computer Science (180 ECTS)'
where id='KN23_3';




update study_program_details
set
    general_information='Додипломските студии по име на студиската програма имаат за цел обезбедување на врвен инженерски кадар за задоволување на се поголемата побарувачка за компјутерски инженери. Студентите на оваа студиска програма се здобиваат со знаења кои им обезбедуваат голема професионална флексибилност и широк избор на работни места секаде каде постои потреба од проектирање, имплементација и одржување на компјутерски системи. Дипломираниот инженер од овој профил својата кариера може да ја гради во индустријата, во јавниот и услужениот сектор, но, особено во компаниите за развој на компјутерски системи, системите што треба да работат во реално време, системите за поддршка на телекомуникациски услуги, услуги базирани на интернет, напредни кориснички интерфејси, апликативни решенија поврзани со дизајн и имплементација на компјутерски системи и друго.',
    graduation_title='Дипломиран инженер по информатички науки и компјутерско инженерство, насока компјутерско инженерство',
    graduation_title_en='Bachelor of Science in Computer Engineering (240 ECTS)'
where id='KI23';




update study_program_details
set
    general_information='Додипломските студии по име на студиската програма имаат за цел обезбедување на врвен инженерски кадар за задоволување на се поголемата побарувачка за компјутерски инженери. Студентите на оваа студиска програма се здобиваат со знаења кои им обезбедуваат голема професионална флексибилност и широк избор на работни места секаде каде постои потреба од проектирање, имплементација и одржување на компјутерски системи. Дипломираниот инженер од овој профил својата кариера може да ја гради во индустријата, во јавниот и услужениот сектор, но, особено во компаниите за развој на компјутерски системи, системите што треба да работат во реално време, системите за поддршка на телекомуникациски услуги, услуги базирани на интернет, напредни кориснички интерфејси, апликативни решенија поврзани со дизајн и имплементација на компјутерски системи и друго.',
    graduation_title='Дипломиран инженер по информатички технологии, насока компјутерско инженерство',
    graduation_title_en='Bachelor of Science in Computer Engineering (180 ECTS) '
where id='KI23_3';


update study_program_details
set
    general_information='Додипломските студии по Интернет, мрежи и безбедност имаат за цел обезбедување на врвен информатички кадар за задоволување на се поголемата побарувачка за мрежни инженери со практично знаење од областа на безбедност на информациите. Овие студии ќе продуцираат кадар наменет пред сé за дизајн и одржување на информациско - комуникациската инфраструктура во стопанството - со посебен акцент на компаниите кои во својата работа користат Интернет и други комуникациски инфраструктури. Покрај базичната инфраструктура, клучна точка на интерес е поставување и одржување на сервисите на повисоките нивоа кои се нудат во мрежна околина, и заштита на системите и корисниците од малициозни напади.',
    graduation_title='Дипломиран инженер по информатички науки и компјутерско инженерство, насока интернет,мрежи и безбедност',
    graduation_title_en='Bachelor of Science in in Internet, networks and security (240 ECTS)'
where id='IMB23';


update study_program_details
set
    general_information='Додипломските студии по Интернет, мрежи и безбедност имаат за цел обезбедување на врвен информатички кадар за задоволување на се поголемата побарувачка за мрежни инженери со практично знаење од областа на безбедност на информациите. Овие студии ќе продуцираат кадар наменет пред сé за дизајн и одржување на информациско - комуникациската инфраструктура во стопанството - со посебен акцент на компаниите кои во својата работа користат Интернет и други комуникациски инфраструктури. Покрај базичната инфраструктура, клучна точка на интерес е поставување и одржување на сервисите на повисоките нивоа кои се нудат во мрежна околина, и заштита на системите и корисниците од малициозни напади.',
    graduation_title='Дипломиран инженер по информатички технологии, насока интернет,мрежи и безбедност',
    graduation_title_en='in Internet, networks and security (180 ECTS)'
where id='IMB23_3';




update study_program_details
set
    general_information='Студиите се наменети за создавање на врвен кадар во областа на информатичкото образование, пред се во средните училишта. Ваквиот образовeн кадар е подготвен да ги пренесе најновите информатички технологии на учениците, ќе овозможи повисоко ниво на познавање на информатичките технологии на сите средношколци, а ќе придонесе и да се подобри нивото на знаење на влез на идните студенти на студиите по информатика на универзитетите.',
    graduation_title='Дипломиран инженер по информатички науки и компјутерско инженерство, насока Информатичка едукација',
    graduation_title_en='Bachelor of Science in Information education (240 ECTS)'
where id='IE23';

update study_program_details
set
    general_information='Целта на оваа студиска програма е да одговори на ова барање и да понуди образовани програмери кои се спремни за вклучување на пазарот на трудот. Брзиот развој во областа бара и соодветни измени и модификации на постоечките високо-образовни курсеви со цел во истите да се вклучат новите достигнувања но, и да се прилагодат на потребите на самата индустрија. Студиската програма вклучува практични курсеви кои се наменети да им овоможат на студентите да се стекнат со потребните вештини за дизајн, развој и тестирање на софтверски апликации. Истата обезбедува едукација на програмери кои не само што ќе можат да одговорат на потребите на брзо растечката софтверска индустрија туку ќе овозможат и нејзин понатамошен развој. За таа цел студиската програма ќе ги подготви студентите и за потребата за доживотно учење со што истите ќе може да ги следат сите измени во софтверската индустрија. Студиите се дизајнирани на начин кој овозможува на студентите кои успешно ќе ги завршат, да можат да продолжат и да се доедуцираат на некоја од останатите насоки на ФИНКИ со цел да може да стекнат повисок степен на образование.',
    graduation_title='Висок стручен програмер',
    graduation_title_en='Bachelor of Applied Science in programming (180 ECTS)'
where id='SSP23';



update study_program_details
set
    general_information='Целта на оваа студиска програма е да одговори на ова барање и да понуди образовани програмери кои се спремни за вклучување на пазарот на трудот. Брзиот развој во областа бара и соодветни измени и модификации на постоечките високо-образовни курсеви со цел во истите да се вклучат новите достигнувања но, и да се прилагодат на потребите на самата индустрија. Студиската програма вклучува практични курсеви кои се наменети да им овоможат на студентите да се стекнат со потребните вештини за дизајн, развој и тестирање на софтверски апликации. Истата обезбедува едукација на програмери кои не само што ќе можат да одговорат на потребите на брзо растечката софтверска индустрија туку ќе овозможат и нејзин понатамошен развој. За таа цел студиската програма ќе ги подготви студентите и за потребата за доживотно учење со што истите ќе може да ги следат сите измени во софтверската индустрија. Студиите се дизајнирани на начин кој овозможува на студентите кои успешно ќе ги завршат, да можат да продолжат и да се доедуцираат на некоја од останатите насоки на ФИНКИ со цел да може да стекнат повисок степен на образование. ',
    graduation_title='Виш стручен програмер',
    graduation_title_en='Bachelor of Applied Science in programming (120 ECTS)'
where id='SSP23_3';




update study_program_details
set
    subject_restrictions='Во наставната програма е предвидено да има 29 задолжителни предмети, и 12 изборни предмети.
Сите изборни предмети се поделени во три групи на тежина (Н1, Н2 и Н3). Тежината е прикажана во кодот на предметите. Предмети што содржат L1 се предмети од група на тежина Н1. Предмети што содржат L2 се предмети од група на тежина Н2. Предмети што во кодот содржат L3, се предмети од група на тежина Н3. Правилата за изборност на предмети според нивото се дефинирани како:
•	Број на предмети од Н1- вкупниот број кредити на избрани изборни предмети од групата Н1 не смее да надминува 6 кредити.
•	Број на предмети од Н2- вкупниот број кредити на избрани изборни предмети од групата Н2 не смее да надминува 36 кредити.
•	Број на предмети од Н3 – нема ограничување во бројот на избрани предмети од групата Н3.
Студентот може да избере и повеќе предмети од групата Н1 и групата Н2 од предвидениот број, но, во тој случај кредитите освоени за овие предмети не влегуваат во вкупниот број на кредити потребни за стекнување на дипломата. Студентот може да добие најмногу 6 ЕКТС кредити од екстракурикуларни активности одобрени од наставно научниот совет на факултетот. Положените предмети и освоените кредити ќе бидат дел од листата на положени предмети во уверението за положени предмети по завршување на факултетот.';


update study_program_details
set
    subject_restrictions='Во наставната програма е предвидено да има 22 задолжителни предмети, и 9 изборни предмети.
Сите изборни предмети се поделени во три групи на тежина (Н1, Н2 и Н3). Тежината е прикажана во кодот на предметите. Предмети што содржат L1 се предмети од група на тежина Н1. Предмети што содржат L2 се предмети од група на тежина Н2. Предмети што во кодот содржат L3, се предмети од група на тежина Н3. Правилата за изборност на предмети според нивото се дефинирани како:
•	Број на предмети од Н1- вкупниот број кредити на избрани изборни предмети од групата Н1 не смее да надминува 6 кредити.
•	Број на предмети од Н2- вкупниот број кредити на избрани изборни предмети од групата Н2 не смее да надминува 36 кредити.
•	Број на предмети од Н3 – нема ограничување во бројот на избрани предмети од групата Н3.
Студентот може да избере и повеќе предмети од групата Н1 и групата Н2 од предвидениот број, но, во тој случај кредитите освоени за овие предмети не влегуваат во вкупниот број на кредити потребни за стекнување на дипломата. Студентот може да добие најмногу 6 ЕКТС кредити од екстракурикуларни активности одобрени од наставно научниот совет на факултетот. Положените предмети и освоените кредити ќе бидат дел од листата на положени предмети во уверението за положени предмети по завршување на факултетот.'
where id like '%23_3';


update study_program_details
set
    subject_restrictions='Во наставната програма е предвидено да има 14 задолжителни предмети, и 6 изборни предмети.
Студентот може да избере од понудените предмети на оваа студиска програма. Студентот може да добие најмногу 6 ЕКТС кредити од екстракурикуларни активности одобрени од наставно научниот совет на факултетот. Положените предмети и освоените кредити ќе бидат дел од листата на положени предмети во уверението за положени предмети по завршување на студиите.
Студентите кои ќе ги завршат овие студии, ќе можат да продолжат на академските студии од прв циклус на насоката Примена на информациски технологии, при што сите положени предмети ќе бидат еквивалентирани со соодветните предмети на таа насока. '
where id = 'SSP23';

update study_program_details
set
    on_english=true,
    subject_restrictions='In the curriculum, it is planned to have 29 compulsory subjects and 12 optional subjects. All optional subjects are divided into three difficulty groups (H1, H2 and H3). The weight is shown in the item code. Subjects containing L1 are subjects of weight group H1. Subjects containing L2 are subjects of weight group H2. Subjects that contain L3 in the code are subjects of weight group H3. The rules for optional subjects according to the level are defined as:
•	Number of subjects from H1 - the total number of credits of selected elective subjects from group H1 must not exceed 6 credits.
•	Number of H2 subjects - the total number of credits of selected elective subjects from the H2 group must not exceed 36 credits.
•	Number of H3 subjects – there is no limit to the number of selected subjects from the H3 group.
The student can choose more courses from group H1 and group H2 than the number provided, but in that case the credits earned for these courses do not count towards the total number of credits needed to obtain the diploma. The student can receive a maximum of 6 ECTS credits from extracurricular activities approved by the teaching and scientific council of the faculty. Passed subjects and earned credits will be part of the list of passed subjects in the certificate of passed subjects after graduation from the faculty.'
where id = 'SEIS23';


update study_program_details
set
    on_english=true,
    subject_restrictions='In the curriculum, it is planned to have 22 compulsory subjects and 9 elective subjects. All optional subjects are divided into three difficulty groups (H1, H2 and H3). The weight is shown in the item code. Subjects containing L1 are subjects of weight group H1. Subjects containing L2 are subjects of weight group H2. Subjects that contain L3 in the code are subjects of weight group H3. The rules for optional subjects according to the level are defined as:
•	Number of subjects from H1 - the total number of credits of selected elective subjects from group H1 must not exceed 6 credits.
•	Number of H2 subjects - the total number of credits of selected elective subjects from the H2 group must not exceed 36 credits.
•	Number of H3 subjects – there is no limit to the number of selected subjects from the H3 group.
The student can choose more courses from group H1 and group H2 than the number provided, but in that case the credits earned for these courses do not count towards the total number of credits needed to obtain the diploma. The student can receive a maximum of 6 ECTS credits from extracurricular activities approved by the teaching and scientific council of the faculty. Passed subjects and earned credits will be part of the list of passed subjects in the certificate of passed subjects after graduation from the faculty.'
where id  = 'SEIS23_3';
