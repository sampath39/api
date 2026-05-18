import os
import re

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find all import statements
    # match group 1: the full import statement including newline
    # match group 2: the class name being imported
    # (ignores wildcard imports like import java.util.*;)
    import_pattern = re.compile(r'^(import\s+(?:static\s+)?[\w\.]+\.(\w+)\s*;\s*)$', re.MULTILINE)
    
    imports = import_pattern.findall(content)
    
    new_content = content
    modified = False
    
    for full_import, class_name in imports:
        # Check if the class name appears anywhere else in the file (not part of the import)
        # We look for word boundaries around the class name
        # First remove the import itself to not match against it
        content_without_import = new_content.replace(full_import, '', 1)
        
        # Check if the class is used elsewhere
        if not re.search(r'\b' + re.escape(class_name) + r'\b', content_without_import):
            new_content = content_without_import
            modified = True
            print(f"Removed unused import: {class_name} from {os.path.basename(filepath)}")

    if modified:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)

def main():
    directory = r'c:\Users\Sampath\Desktop\codelab\TalentStreamBE\src\main\java'
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                process_file(os.path.join(root, file))

if __name__ == '__main__':
    main()
