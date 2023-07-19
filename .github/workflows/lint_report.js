module.exports = async ({github, context, xmlFiles}) => {

    await github.rest.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: '⚠️ ${xmlFiles.length} xml files found'
    })
}