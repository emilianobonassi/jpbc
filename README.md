Forked repository for [jPBC](http://gas.dia.unisa.it/projects/jpbc/).

After [Codehaus termination](www.codehaus.org) Maven dependencies didn't work anymore.
Now, you can use the fabulous JitPack.io in two steps.

**Step 1.** Add the JitPack repository to your build file
```	
<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
</repository>
```

**Step 2.** Add the dependency in the form
```
<dependency>
	<groupId>com.github.emilianobonassi</groupId>
	<artifactId>jpbc</artifactId>
	<version>v2.0.0</version>
</dependency> 
```
