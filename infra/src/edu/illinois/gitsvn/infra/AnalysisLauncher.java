package edu.illinois.gitsvn.infra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

import edu.illinois.gitsvn.analysis.ArduinoAnalysis;
import edu.illinois.gitsvn.analysis.CyclopsGroupAnalysis;
import edu.illinois.gitsvn.analysis.EclipseJDTCoreAnalysis;
import edu.illinois.gitsvn.analysis.EclipseJDTDebugAnalysis;
import edu.illinois.gitsvn.analysis.EclipseJDTUIAnalysis;
import edu.illinois.gitsvn.analysis.EclipsePlatform;
import edu.illinois.gitsvn.analysis.EclipsePlatformCommon;
import edu.illinois.gitsvn.analysis.EclipsePlatformDebug;
import edu.illinois.gitsvn.analysis.EclipsePlatformTeam;
import edu.illinois.gitsvn.analysis.EclipsePlatformText;
import edu.illinois.gitsvn.analysis.FFmpegAnalysis;
import edu.illinois.gitsvn.analysis.JUnitAnalysis;
import edu.illinois.gitsvn.analysis.MPSAnalysis;
import edu.illinois.gitsvn.analysis.PrototypeAnalysis;
import edu.illinois.gitsvn.analysis.ThymeleafAnalysis;
import edu.illinois.gitsvn.analysis.UPMAnalysis;

public class AnalysisLauncher {

	public static void main(String[] args) throws Exception {
		new AnalysisLauncher().run();
	}

	/**
	 * Starts the analysis.
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		List<AnalysisConfiguration> configurations = new ArrayList<AnalysisConfiguration>();

		populateWithConfigurations(configurations);

		long before = System.nanoTime();
		runParallel(configurations);
		long after = System.nanoTime();

		System.out.println((after - before) / 1000000);
	}

	protected void populateWithConfigurations(List<AnalysisConfiguration> configurations) {
		configurations.add(new EclipseJDTCoreAnalysis());
		configurations.add(new MPSAnalysis());
		configurations.add(new ArduinoAnalysis());
		configurations.add(new FFmpegAnalysis());
		configurations.add(new CyclopsGroupAnalysis());
		configurations.add(new ThymeleafAnalysis());
		configurations.add(new EclipseJDTDebugAnalysis());
		configurations.add(new EclipseJDTUIAnalysis());
		configurations.add(new EclipsePlatform());
		configurations.add(new EclipsePlatformTeam());
		configurations.add(new EclipsePlatformText());
		configurations.add(new EclipsePlatformDebug());
		configurations.add(new EclipsePlatformCommon());
		configurations.add(new JUnitAnalysis());
		configurations.add(new PrototypeAnalysis());
		configurations.add(new UPMAnalysis());
	}

	private void runSerial(List<AnalysisConfiguration> configurations) {
		for (int i = 0; i < configurations.size(); i++) {
			System.out.println("\n" + (i + 1) + " / " + configurations.size());
			configurations.get(i).run();
		}
	}

	private void runParallel(List<AnalysisConfiguration> configurations) {
		ForkJoinPool pool = new ForkJoinPool();
		List<Callable<Void>> list = new ArrayList<>();

		for (final AnalysisConfiguration analysisConfiguration : configurations) {
			list.add(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					analysisConfiguration.run();
					return null;
				}
			});
		}

		pool.invokeAll(list);
	}

}
