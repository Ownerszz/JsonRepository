package ownerszz.libraries.json.repository.core.dependency.injection;

import ownerszz.libraries.dependency.injection.annotation.scanner.AnnotationScanner;
import ownerszz.libraries.dependency.injection.core.DependencyManager;
import ownerszz.libraries.dependency.injection.core.DependencyRegistrator;
import ownerszz.libraries.dependency.injection.core.ResolveDependencies;
import ownerszz.libraries.json.repository.core.JsonRepositoryFactory;

@DependencyRegistrator
public class JsonRepositoryRegistrator {
    @ResolveDependencies
    public JsonRepositoryRegistrator(DependencyManager dependencyManager) throws Exception {
        dependencyManager.registerPoxyOnAnnotation(LocalJsonRepository.class, impl->{
            Class actual =  impl.getClass().getInterfaces()[0];
            LocalJsonRepository ann = AnnotationScanner.getAnnotation(actual, LocalJsonRepository.class);
            try {
                return JsonRepositoryFactory.createLocalJsonRepository(actual,ann.target());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
