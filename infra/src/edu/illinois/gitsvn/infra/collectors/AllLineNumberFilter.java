package edu.illinois.gitsvn.infra.collectors;

import java.util.Collection;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitective.core.filter.commit.DiffCountFilter;

import edu.illinois.gitsvn.infra.DataCollector;

public class AllLineNumberFilter extends DiffCountFilter implements DataCollector {

	private int count;

	/**
	 */
	public AllLineNumberFilter() {
		super(true);
	}

	@Override
	protected TreeWalk createTreeWalk(RevWalk walker, RevCommit commit) {
		TreeWalk walk = super.createTreeWalk(walker, commit);
		return walk;
	}

	@Override
	protected boolean include(RevCommit commit, Collection<DiffEntry> diffs, int diffCount) {
		count = diffCount;

		return true;
	}

	@Override
	public String name() {
			return "LOC";
	}

	@Override
	public String getDataForCommit() {
		return "" + count;
	}
}