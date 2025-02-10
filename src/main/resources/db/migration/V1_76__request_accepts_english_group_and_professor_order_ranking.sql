alter table if exists teacher_subject_requests
    add column if not exists accepts_english_group boolean default false;

alter table if exists joined_subject
    add column if not exists validation_message varchar(4000);

update professor
set ordering_rank=1
where email = 'marjan.gushev@finki.ukim.mk';
update professor
set ordering_rank=2
where email = 'katerina.zdravkova@finki.ukim.mk';
update professor
set ordering_rank=3
where email = 'suzana.loshkovska@finki.ukim.mk';
update professor
set ordering_rank=4
where email = 'kosta.mitreski@finki.ukim.mk';
update professor
set ordering_rank=5
where email = 'verica.bakeva@finki.ukim.mk';
update professor
set ordering_rank=6
where email = 'vladimir.trajkovik@finki.ukim.mk';
update professor
set ordering_rank=7
where email = 'ana.madevska@finki.ukim.mk';
update professor
set ordering_rank=8
where email = 'dejan.gjorgjevikj@finki.ukim.mk';
update professor
set ordering_rank=9
where email = 'dimitar.trajanov@finki.ukim.mk';
update professor
set ordering_rank=10
where email = 'andrea.kulakov@finki.ukim.mk';
update professor
set ordering_rank=11
where email = 'ljupcho.antovski@finki.ukim.mk';
update professor
set ordering_rank=12
where email = 'marija.mihova@finki.ukim.mk';
update professor
set ordering_rank=13
where email = 'slobodan.kalajdziski@finki.ukim.mk';
update professor
set ordering_rank=14
where email = 'nevena.ackovska@finki.ukim.mk';
update professor
set ordering_rank=15
where email = 'goran.velinov@finki.ukim.mk';
update professor
set ordering_rank=16
where email = 'anastas.mishev@finki.ukim.mk';
update professor
set ordering_rank=17
where email = 'sonja.filiposka@finki.ukim.mk';
update professor
set ordering_rank=18
where email = 'ivan.chorbev@finki.ukim.mk';
update professor
set ordering_rank=19
where email = 'lasko.basnarkov@finki.ukim.mk';
update professor
set ordering_rank=20
where email = 'boro.jakimovski@finki.ukim.mk';
update professor
set ordering_rank=21
where email = 'vesna.dimitrova@finki.ukim.mk';
update professor
set ordering_rank=22
where email = 'goce.armenski@finki.ukim.mk';
update professor
set ordering_rank=23
where email = 'sonja.gievska@finki.ukim.mk';
update professor
set ordering_rank=24
where email = 'dejan.spasov@finki.ukim.mk';
update professor
set ordering_rank=25
where email = 'ivica.dimitrovski@finki.ukim.mk';
update professor
set ordering_rank=26
where email = 'igor.mishkovski@finki.ukim.mk';
update professor
set ordering_rank=27
where email = 'gjorgji.madjarov@finki.ukim.mk';
update professor
set ordering_rank=28
where email = 'smilka.janeska@finki.ukim.mk';
update professor
set ordering_rank=29
where email = 'vangel.ajanovski@finki.ukim.mk';
update professor
set ordering_rank=30
where email = 'vesna.dimitrievska@finki.ukim.mk';
update professor
set ordering_rank=31
where email = 'mile.jovanov@finki.ukim.mk';
update professor
set ordering_rank=32
where email = 'biljana.stojkoska@finki.ukim.mk';
update professor
set ordering_rank=33
where email = 'kire.trivodaliev@finki.ukim.mk';
update professor
set ordering_rank=34
where email = 'sasho.gramatikov@finki.ukim.mk';
update professor
set ordering_rank=35
where email = 'miroslav.mirchev@finki.ukim.mk';
update professor
set ordering_rank=36
where email = 'georgina.mirceva@finki.ukim.mk';
update professor
set ordering_rank=37
where email = 'magdalena.kostoska@finki.ukim.mk';
update professor
set ordering_rank=38
where email = 'aleksandra.popovska@finki.ukim.mk';
update professor
set ordering_rank=39
where email = 'biljana.tojtovska@finki.ukim.mk';
update professor
set ordering_rank=40
where email = 'natasa.ilievska@finki.ukim.mk';
update professor
set ordering_rank=41
where email = 'milos.jovanovik@finki.ukim.mk';
update professor
set ordering_rank=42
where email = 'andreja.naumoski@finki.ukim.mk';
update professor
set ordering_rank=43
where email = 'pance.ribarski@finki.ukim.mk';
update professor
set ordering_rank=44
where email = 'hristina.mihajloska@finki.ukim.mk';
update professor
set ordering_rank=45
where email = 'ivan.kitanovski@finki.ukim.mk';
update professor
set ordering_rank=46
where email = 'eftim.zdravevski@finki.ukim.mk';
update professor
set ordering_rank=47
where email = 'petre.lameski@finki.ukim.mk';
update professor
set ordering_rank=48
where email = 'vladimir.zdraveski@finki.ukim.mk';
update professor
set ordering_rank=49
where email = 'aleksandra.kanevche@finki.ukim.mk';
update professor
set ordering_rank=50
where email = 'katarina.trojacanec@finki.ukim.mk';
update professor
set ordering_rank=51
where email = 'bojana.koteska@finki.ukim.mk';
update professor
set ordering_rank=52
where email = 'riste.stojanov@finki.ukim.mk';
update professor
set ordering_rank=53
where email = 'metodija.jancheski@finki.ukim.mk';
update professor
set ordering_rank=54
where email = 'monika.simjanoska@finki.ukim.mk';
update professor
set ordering_rank=55
where email = 'boban.joksimoski@finki.ukim.mk';
update professor
set ordering_rank=56
where email = 'ilinka.ivanoska@finki.ukim.mk';
update professor
set ordering_rank=57
where email = 'emil.stankov@finki.ukim.mk';
update professor
set ordering_rank=58
where email = 'bojan.ilijoski@finki.ukim.mk';
update professor
set ordering_rank=59
where email = 'kostadin.mishev@finki.ukim.mk';
update professor
set ordering_rank=60
where email = 'aleksandar.tenev@finki.ukim.mk';
update professor
set ordering_rank=61
where email = 'aleksandar.s@finki.ukim.mk';
update professor
set ordering_rank=62
where email = 'vlatko.spasev@finki.ukim.mk';
update professor
set ordering_rank=63
where email = 'stefan.andonov@finki.ukim.mk';
update professor
set ordering_rank=64
where email = 'dimitar.kitanovski@finki.ukim.mk';
update professor
set ordering_rank=65
where email = 'nenad.anchev@finki.ukim.mk';
update professor
set ordering_rank=66
where email = 'vojdan.kjorveziroski@finki.ukim.mk';
update professor
set ordering_rank=67
where email = 'jovana.dobreva@finki.ukim.mk';
update professor
set ordering_rank=68
where email = 'jana.kuzmanova@finki.ukim.mk';
update professor
set ordering_rank=69
where email = 'ana.todorovska@finki.ukim.mk';
update professor
set ordering_rank=70
where email = 'petar.sekuloski@finki.ukim.mk';
update professor
set ordering_rank=71
where email = 'martina.toshevska@finki.ukim.mk';
update professor
set ordering_rank=72
where email = 'slave.temkov@finki.ukim.mk';
update professor
set ordering_rank=73
where email = 'sijche.pechkova@finki.ukim.mk';
update professor
set ordering_rank=74
where email = 'milena.trajanoska@finki.ukim.mk';
update professor
set ordering_rank=1000
where email = 'kiril.kjiroski@finki.ukim.mk';
update professor
set ordering_rank=1001
where email = 'vladislav.bidikov@finki.ukim.mk';
