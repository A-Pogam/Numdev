module.exports = {
  preset: 'jest-preset-angular',
  setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
  globals: {
    'ts-jest': {
      tsconfig: '<rootDir>/tsconfig.spec.json',
      stringifyContentPathRegex: '\\.html$'
    }
  },
  testEnvironment: 'jsdom',
  transform: {
    '^.+\\.(ts|js|html)$': 'ts-jest'
  },
  moduleNameMapper: {
    '@core/(.*)': '<rootDir>/src/app/core/$1',
    '^src/(.*)$': '<rootDir>/src/$1'
  },
  moduleFileExtensions: ['ts', 'html', 'js', 'json'],
  testPathIgnorePatterns: ['<rootDir>/node_modules/', '<rootDir>/dist/'],
  coveragePathIgnorePatterns: ['<rootDir>/node_modules/'],
  coverageDirectory: './coverage/jest',
  collectCoverage: true,
  collectCoverageFrom: [
    'src/app/**/*.{ts,js}',
    '!**/*.module.ts',
    '!**/main.ts',
    '!**/polyfills.ts',
    '!**/test.ts',
    '!**/environments/**'
  ],
  coverageThreshold: {
    global: {
      statements: 80,
      branches: 80,
      functions: 80,
      lines: 80
    }
  },
  modulePaths: ['<rootDir>'],
  moduleDirectories: ['node_modules'],
  roots: ['<rootDir>']
};
