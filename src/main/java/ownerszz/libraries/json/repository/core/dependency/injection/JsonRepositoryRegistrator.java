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
        dependencyManager.registerPoxyOnAnnotation(JsonRepository.class, impl->{
            Class actual =  impl.getClass().getInterfaces()[0];
            JsonRepository ann = AnnotationScanner.getAnnotation(actual,JsonRepository.class);
            try {
                return JsonRepositoryFactory.createProxyFor(actual,ann.target());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
