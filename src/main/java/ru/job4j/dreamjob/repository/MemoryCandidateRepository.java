package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Candidate> summary = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Alex", "Intern Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Daria", "Junior Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Ben", "Junior+ Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Bob", "Middle Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Ken", "Middle+ Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Jes", "Senior Java Developer", LocalDateTime.now()));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        summary.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return summary.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return summary.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription(), oldCandidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(summary.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return summary.values();
    }
}
