package com.lagm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringReactorDemoApplication.class);
	private static List<String> courses = new ArrayList<>();

	public void createMono() {
		log.info("Begin createMono");
		Mono<String> m1 = Mono.just("Hello coders");
		m1.subscribe(x -> log.info("Data " + x));

		Mono.just(5).subscribe(x -> log.info("Data: " + x));

		log.info("End createMono");
	}

	public void createFlux() {
		log.info("Begin createFlux");

		Flux.just(1, 2, 3, 4).subscribe(x -> log.info(x.toString()));
		Flux<String> flux1 = Flux.fromIterable(courses);
		flux1.subscribe(x -> log.info("course: " + x));

		// Conversión MONO a LIST
		flux1.collectList().subscribe(list -> log.info("La lista: " + list));

		log.info("End createFlux");
	}

	public void metodoDoOnNext() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		// fx1.doOnNext(x -> log.info(x))
		fx1.doOnNext(log::info)
				.subscribe();
	}

	public void metodoMap() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.map(String::toUpperCase); // fx1.map(x -> x.toUpperCase());
		fx1.subscribe(log::info);
	}

	public void metodoFlatMap() {
		Mono.just("jaime").map(x -> 32).subscribe(e -> log.info("Data: " + e));
		Mono.just("jaime").map(x -> Mono.just(32)).subscribe(e -> log.info("Data: " + e));
		Mono.just("jaime").flatMap(x -> Mono.just(32)).subscribe(e -> log.info("Data: " + e));
	}

	public void metodoRange() {
		Flux<Integer> fx1 = Flux.range(0, 5);
		fx1.map(x -> x + 1).subscribe(x -> log.info(x.toString()));
	}

	public void metodoDelayElements() throws InterruptedException {
		Flux.range(0, 10)
				.delayElements(Duration.ofSeconds(2))
				.doOnNext(x -> log.info("Data: " + x))
				.subscribe();
		Thread.sleep(22000); // temporal
	}

	public void metodoZipWith() {
		List<String> clients = new ArrayList<>();
		clients.add("Cliente 1");
		clients.add("Cliente 2");
		clients.add("Cliente 3");

		Flux<String> fx1 = Flux.fromIterable(courses);
		Flux<String> fx2 = Flux.fromIterable(clients);
		fx1.zipWith(fx2, (course, client) -> course + "-" + client)
				.subscribe(log::info);
	}

	public void metodoMerge() {
		List<String> clients = new ArrayList<>();
		clients.add("Cliente 1");
		clients.add("Cliente 2");
		clients.add("Cliente 3");

		Flux<String> fx1 = Flux.fromIterable(courses);
		Flux<String> fx2 = Flux.fromIterable(clients);
		Flux.merge(fx1, fx2).subscribe(log::info);
	}

	public void metodoFilter() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.filter(course -> course.startsWith("J"))
				.subscribe(log::info);

		//Predicate<String> predicate = p -> p.startsWith("J");
		//fx1.filter(predicate).subscribe(log::info);
	}

	public void metodoTakeLast() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.takeLast(3).subscribe(log::info);
	}

	public void metodoTake() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.take(2).subscribe(log::info);
	}

	public void metodoSkip() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.skip(2).subscribe(log::info);
	}

	public void metodoDefaultIfEmpty() {
		// courses = new ArrayList<>();
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.map(e -> "P:" + e)
				.defaultIfEmpty("EMPTY FLUX")
				.subscribe(log::info);
	}

	public void metodoError() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.doOnNext(c -> {
			throw new ArithmeticException("BAD NUMBER");
		})
				.onErrorReturn("VALOR POR DEFECTO PARA ERROR")
				.subscribe(x -> log.info("Data: " + x));
	}

	public void metodoError2() {
		Flux<String> fx1 = Flux.fromIterable(courses);
		fx1.doOnNext(c -> {
					throw new ArithmeticException("BAD NUMBER");
				})
				.onErrorMap(ex -> new Exception(ex.getMessage()))
				.subscribe(x -> log.info("Data: " + x));
	}

	public static void main(String[] args) {
		courses.add("Java");
		courses.add("C++");
		courses.add("Pascal");
		courses.add("C#");
		SpringApplication.run(SpringReactorDemoApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		// createMono();
		// createFlux();
		// metodoDoOnNext();
		// metodoMap();
		// metodoFlatMap();
		// metodoRange();
		// metodoDelayElements();
		// metodoZipWith();
		// metodoMerge();
		// metodoFilter();
		// metodoTakeLast();
		// metodoTake();
		// metodoSkip();
		// metodoDefaultIfEmpty();
		// metodoError();
		metodoError2();
	}
}
