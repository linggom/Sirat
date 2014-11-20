Sirat
=====

Sirat is a library  to generate your Simple Pojo class to be a query that be used in ContentProvider.

You can convert from this :

		class Dog{
			@PRIMARY_KEY private String name;
			@NOT_NULL private String type;
			private long birthDate;
			@UNIQUE private int uuid;
			@Ignore private String birthPlace;
		}
		
The class will be generate as query like this : 
		
		CREATE TABLE 'Dog' (
			'type' TEXT NOT NULL , 
			'name' TEXT, 
			'uuid' INTEGER, 
			'birthDate' LONG,  
			PRIMARY KEY ('name'),  
			UNIQUE ('uuid')
		)
